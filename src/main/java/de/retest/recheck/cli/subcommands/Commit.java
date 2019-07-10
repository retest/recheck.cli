package de.retest.recheck.cli.subcommands;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.esotericsoftware.kryo.KryoException;

import de.retest.recheck.Properties;
import de.retest.recheck.cli.FilterUtil;
import de.retest.recheck.cli.PreCondition;
import de.retest.recheck.cli.RecheckCli;
import de.retest.recheck.cli.TestReportFormatException;
import de.retest.recheck.cli.TestReportUtil;
import de.retest.recheck.ignore.Filter;
import de.retest.recheck.persistence.NoGoldenMasterFoundException;
import de.retest.recheck.persistence.Persistence;
import de.retest.recheck.persistence.PersistenceFactory;
import de.retest.recheck.persistence.xml.util.StdXmlClassesProvider;
import de.retest.recheck.report.TestReport;
import de.retest.recheck.report.TestReportFilter;
import de.retest.recheck.suite.flow.ApplyChangesToStatesFlow;
import de.retest.recheck.suite.flow.CreateChangesetForAllDifferencesFlow;
import de.retest.recheck.ui.descriptors.SutState;
import de.retest.recheck.ui.review.ReviewResult;
import de.retest.recheck.ui.review.SuiteChangeSet;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command( name = "commit", description = "Accept specified differences of given test report." )
public class Commit implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger( Commit.class );

	@Option( names = "--help", usageHelp = true, hidden = true )
	private boolean displayHelp;

	@Option( names = "--all", description = "Accept all differences from the given test report." )
	private boolean all;

	@Option( names = "--exclude", description = "Filter(s) to exclude changes from the diff." )
	private List<String> exclude;

	@Parameters( arity = "1", description = RecheckCli.REPORT_FILE_PARAM_DESCRIPTION )
	private File testReport;

	@Override
	public void run() {
		if ( !PreCondition.isSatisfied() ) {
			return;
		}
		if ( !inputValidation( all, testReport ) ) {
			return;
		}
		try {
			final TestReport report = TestReportUtil.load( testReport );
			final Filter excludeFilter = FilterUtil.getExcludeFilterFiles( exclude );
			final TestReport filteredTestReport = TestReportFilter.filter( report, excludeFilter );
			if ( !filteredTestReport.containsChanges() ) {
				logger.warn( "The test report has no differences." );
				return;
			}
			TestReportUtil.print( filteredTestReport, testReport );
			final ReviewResult reviewResult = CreateChangesetForAllDifferencesFlow.create( filteredTestReport );
			for ( final SuiteChangeSet suiteChangeSet : reviewResult.getSuiteChangeSets() ) {
				applyChanges( createSutStatePersistence(), suiteChangeSet );
			}
		} catch ( final TestReportFormatException e ) {
			logger.error( "The given file is not a test report. Please only pass files using the '{}' extension.",
					Properties.TEST_REPORT_FILE_EXTENSION );
		} catch ( final IOException e ) {
			logger.error( "An error occurred while loading the test report!", e );
		} catch ( final KryoException e ) {
			logger.error( "The report was created with another, incompatible recheck version.\r\n"
					+ "Please, use the same recheck version to load a report with which it was generated." );
			logger.debug( "StackTrace: ", e );
		}
	}

	private boolean inputValidation( final boolean all, final File testReport ) {
		if ( !all ) {
			logger.warn( "Currently only the 'commit --all' command is implemented." );
			logger.warn( "A command to commit specific differences will be implemented shortly." );
			return false;
		}
		if ( testReport == null ) {
			logger.error( "Please specify exactly one test report." );
			return false;
		}
		return true;
	}

	private void applyChanges( final Persistence<SutState> persistence, final SuiteChangeSet suiteChangeSet ) {
		try {
			ApplyChangesToStatesFlow.apply( persistence, suiteChangeSet );
		} catch ( final NoGoldenMasterFoundException e ) {
			logger.error( "The Golden Master '{}' cannot be found.", e.getFilename() );
			logger.error(
					"Please make sure that the given test report '{}' is within the corresponding project directory.",
					testReport.getAbsolutePath() );
		}
	}

	private static Persistence<SutState> createSutStatePersistence() {
		return new PersistenceFactory( new HashSet<>( Arrays.asList( StdXmlClassesProvider.getXmlDataClasses() ) ) )
				.getPersistence();
	}

	boolean isDisplayHelp() {
		return displayHelp;
	}

	boolean isAll() {
		return all;
	}

	File getTestReport() {
		return testReport;
	}
}
