package de.retest.recheck.cli.subcommands;

import static de.retest.recheck.XmlTransformerUtil.getXmlTransformer;
import static picocli.CommandLine.ExitCode.OK;
import static picocli.CommandLine.ExitCode.SOFTWARE;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.retest.recheck.RecheckProperties;
import de.retest.recheck.SuiteAggregator;
import de.retest.recheck.cli.utils.ErrorHandler;
import de.retest.recheck.cli.utils.FilterUtil;
import de.retest.recheck.cli.utils.GoldenMasterUtil;
import de.retest.recheck.execution.RecheckDifferenceFinder;
import de.retest.recheck.ignore.Filter;
import de.retest.recheck.persistence.GoldenMasterProvider;
import de.retest.recheck.persistence.GoldenMasterProviderImpl;
import de.retest.recheck.persistence.PersistenceFactory;
import de.retest.recheck.printer.ActionReplayResultPrinter;
import de.retest.recheck.report.ActionReplayResult;
import de.retest.recheck.report.SuiteReplayResult;
import de.retest.recheck.report.TestReplayResult;
import de.retest.recheck.report.TestReport;
import de.retest.recheck.report.TestReportFilter;
import de.retest.recheck.ui.descriptors.SutState;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.IExitCodeGenerator;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command( name = "diff", //
		descriptionHeading = "%nDescription:%n", //
		parameterListHeading = "%nParameters:%n", //
		optionListHeading = "%nOptions:%n", // 
		description = "Compare two Golden Masters." )
public class Diff implements Runnable, IExitCodeGenerator {
	private static final Logger logger = LoggerFactory.getLogger( Diff.class );

	private static final PersistenceFactory persistenceFactory = new PersistenceFactory( getXmlTransformer() );
	private static final GoldenMasterProvider goldenMasterProvider =
			new GoldenMasterProviderImpl( persistenceFactory.getPersistence() );

	private static final String TEST_NAME = "Test - Golden Master Comparison";
	private static final String SUITE_NAME = "Suite - Golden Master Comparison";

	// TODO Temporarily it will be allowed to use the diff command for display of test reports
	// TODO Will be removed in following version (see RET-1956); instead, use the command "show"
	@Parameters( arity = "1..2", description = GoldenMasterUtil.GOLDEN_MASTER_PARAMETER_DESCRIPTION )
	private Path[] goldenMasterPath;

	@Option( names = "--exclude", description = FilterUtil.EXCLUDE_OPTION_DESCRIPTION )
	private List<String> exclude;

	@Option( names = "--help", usageHelp = true, hidden = true )
	private boolean displayHelp;

	@Option( names = "--output", arity = "1",
			description = "Save differences of Golden Masters to specified directory as test report." )
	private Path directory;

	private int exitCode = OK;

	@Override
	public void run() {
		// TODO: until the following version (see RET-1956), a user may still use the diff command to print test report differences.
		// TODO: remove in next version (see RET-1956)
		if ( goldenMasterPath.length == 1 ) {
			logger.warn(
					"The diff command for viewing a test report is deprecated and will be removed in the following version."
							+ " Please use the new command 'show' to display a test report." );

			final List<String> argsAsList = new ArrayList<>();
			argsAsList.add( goldenMasterPath[0].toString() );
			if ( exclude != null ) {
				for ( final String filter : exclude ) {
					argsAsList.add( "--exclude" );
					argsAsList.add( filter );
				}
			}

			final String[] args = argsAsList.toArray( new String[0] );
			new CommandLine( new Show() ).execute( args );

		} else {

			final Path expectedSutStatePath = goldenMasterPath[0];
			final Path actualSutStatePath = goldenMasterPath[1];

			try {
				if ( !FilterUtil.hasValidExcludeOption( exclude ) ) {
					exitCode = SOFTWARE;
					return;
				}
				final ActionReplayResult actionReplayResult =
						createActionReplayResultFrom( expectedSutStatePath, actualSutStatePath );
				printGoldenMasterDifferences( filterActionReplayResult( actionReplayResult ) );
				persistIfOutputOptionPresent( actionReplayResult );
			} catch ( final Exception e ) {
				exitCode = SOFTWARE;
				ErrorHandler.handle( e );
			}
		}
	}

	@Override
	public int getExitCode() {
		return exitCode;
	}

	private ActionReplayResult createActionReplayResultFrom( final Path expectedSutStatePath,
			final Path actualSutStatePath ) {
		final SutState expectedSutState = loadGoldenMaster( expectedSutStatePath );
		final SutState actualSutState = loadGoldenMaster( actualSutStatePath );

		return findDifferences( expectedSutStatePath.toString(), expectedSutState, actualSutState );
	}

	private SutState loadGoldenMaster( final Path actualSutStatePath ) {
		return goldenMasterProvider
				.loadGoldenMaster( actualSutStatePath.resolve( RecheckProperties.DEFAULT_XML_FILE_NAME ).toFile() );
	}

	private ActionReplayResult findDifferences( final String expectedSutStatePath, final SutState expectedSutState,
			final SutState actualSutState ) {
		final RecheckDifferenceFinder differenceFinder = new RecheckDifferenceFinder(
				( attributes, s, serializable ) -> false, "Comparison of Golden Masters", expectedSutStatePath );
		return differenceFinder.findDifferences( expectedSutState, actualSutState );
	}

	private void printGoldenMasterDifferences( final ActionReplayResult actionReplayResult ) {
		final ActionReplayResultPrinter printer =
				new ActionReplayResultPrinter( ( attributes, s, serializable ) -> false );
		logger.info( "\n\n{}\n\n", printer.toString( actionReplayResult ) );

		final int differenceCount = actionReplayResult.getDifferences().size();
		if ( differenceCount > 0 ) {
			logger.warn( "Overall, recheck found {} difference(s) when checking {} element(s).", differenceCount,
					actionReplayResult.getCheckedUiElementsCount() );
		}
	}

	private ActionReplayResult filterActionReplayResult( final ActionReplayResult actionReplayResult ) {
		final Filter filterFiles = FilterUtil.getFilterFiles( exclude );
		final TestReportFilter reportFilter = new TestReportFilter( (filterFiles) );
		return reportFilter.filter( actionReplayResult );
	}

	private void persistIfOutputOptionPresent( final ActionReplayResult actionReplayResult ) throws IOException {
		if ( directory != null ) {
			persist( actionReplayResult, directory );
			return;
		}
		logger.info( "The displayed differences may be saved as test report by using the --output option." );
	}

	private void persist( final ActionReplayResult result, final Path outputDirectory ) throws IOException {
		final TestReplayResult testPlayResult = new TestReplayResult( TEST_NAME, 1 );
		final SuiteReplayResult suiteReplayResult = SuiteAggregator.getInstance().getSuite( SUITE_NAME );
		testPlayResult.addAction( result );
		suiteReplayResult.addTest( testPlayResult );
		final TestReport testReport = new TestReport( suiteReplayResult );

		final File report = outputDirectory.resolve( RecheckProperties.AGGREGATED_TEST_REPORT_FILE_NAME ).toFile();

		persistenceFactory.getPersistence().save( report.toURI(), testReport );

		logger.info( "The test report has been saved to '{}'.", report.getAbsolutePath() );
	}

}
