package de.retest.recheck.cli.subcommands;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.esotericsoftware.kryo.KryoException;

import de.retest.recheck.Properties;
import de.retest.recheck.cli.PreCondition;
import de.retest.recheck.cli.RecheckCli;
import de.retest.recheck.cli.TestReportFormatException;
import de.retest.recheck.cli.TestReportUtil;
import de.retest.recheck.ignore.RecheckIgnoreUtil;
import de.retest.recheck.report.ActionReplayResult;
import de.retest.recheck.report.SuiteReplayResult;
import de.retest.recheck.report.TestReplayResult;
import de.retest.recheck.report.TestReport;
import de.retest.recheck.review.GlobalIgnoreApplier;
import de.retest.recheck.review.counter.NopCounter;
import de.retest.recheck.review.workers.LoadFilterWorker;
import de.retest.recheck.review.workers.SaveFilterWorker;
import de.retest.recheck.ui.descriptors.Element;
import de.retest.recheck.ui.diff.AttributeDifference;
import de.retest.recheck.ui.diff.ElementDifference;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command( name = "ignore", description = "Ignore specified differences of given test report." )
public class Ignore implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger( Ignore.class );

	@Option( names = "--help", usageHelp = true, hidden = true )
	private boolean displayHelp;

	@Option( names = "--all", description = "Ignore all differences from the given test report." )
	private boolean all;

	@Option( names = "--list", description = "List all ignored elements." )
	private boolean list;

	@Parameters( arity = "0..1", description = RecheckCli.REPORT_FILE_PARAM_DESCRIPTION )
	private File testReport;

	private GlobalIgnoreApplier ignoreApplier;

	@Override
	public void run() {
		if ( !PreCondition.isSatisfied() ) {
			return;
		}
		if ( list ) {
			final Optional<Path> ignoreFile = RecheckIgnoreUtil.getIgnoreFile();
			if ( !ignoreFile.map( Path::toFile ).map( File::exists ).orElse( false ) ) {
				logger.info( "No elements are ignored." );
				return;
			}

			final Path path = ignoreFile.get();
			logger.info( "These elements are ignored:" );
			try {
				Files.readAllLines( path ).forEach( logger::info );
			} catch ( final IOException e ) {
				logger.error( "Error reading from recheck ignore file in {}.", ignoreFile );
			}
		}
		if ( inputValidation( all, testReport, list ) ) {
			try {
				final TestReport report = TestReportUtil.load( testReport );
				if ( !report.containsChanges() ) {
					logger.warn( "The test report has no differences." );
					return;
				}
				TestReportUtil.print( report, testReport );
				loadRecheckIgnore();
				if ( allDifferencesAreBlacklisted( report ) ) {
					logger.warn( "All differences in the given test report are already ignored." );
					return;
				}
				saveRecheckIgnore();
			} catch ( final TestReportFormatException e ) {
				logger.error( "The given file is not a test report. Please only pass files using the '{}' extension.",
						Properties.TEST_REPORT_FILE_EXTENSION );
			} catch ( final IOException e ) {
				logger.error( "An error occurred while loading the test report.", e );
			} catch ( final KryoException e ) {
				logger.error( "The report was created with another, incompatible recheck version.\n"
						+ "Please use the same recheck version to load a report with which it was generated." );
				logger.debug( "Stack trace:", e );
			}
		}
	}

	private void loadRecheckIgnore() {
		try {
			final LoadFilterWorker loadFilterWorker = new LoadFilterWorker( NopCounter.getInstance() );
			ignoreApplier = loadFilterWorker.load();
			logger.info( "The recheck ignore file was loaded successfully." );
		} catch ( final IOException e ) {
			logger.error( "Could not load recheck ignore file.", e );
		}
	}

	private void saveRecheckIgnore() {
		try {
			final SaveFilterWorker saveFilterWorker = new SaveFilterWorker( ignoreApplier );
			saveFilterWorker.save();
			logger.info( "The recheck ignore file has been updated." );
		} catch ( final IOException e ) {
			logger.error( "Could not save the recheck ignore file.", e );
		}
	}

	private boolean inputValidation( final boolean all, final File testReport, final boolean list ) {
		if ( list ) {
			return false;
		}
		if ( !all ) {
			logger.warn( "Currently only the two commands 'ignore --all' and 'ignore --list' are implemented." );
			logger.warn( "A command to ignore specific differences will be implemented shortly." );
			return false;
		}
		if ( testReport == null ) {
			logger.warn( "Please specify exactly one test report to ignore all differences." );
			return false;
		}
		return true;
	}

	private boolean allDifferencesAreBlacklisted( final TestReport report ) {
		boolean allDifferencesAlreadyListed = true;
		for ( final SuiteReplayResult suiteReplayResult : report.getSuiteReplayResults() ) {
			for ( final TestReplayResult testReplayResult : suiteReplayResult.getTestReplayResults() ) {
				for ( final ActionReplayResult actionReplayResult : testReplayResult.getActionReplayResults() ) {
					for ( final ElementDifference elementDifference : actionReplayResult.getAllElementDifferences() ) {
						for ( final AttributeDifference attributeDifference : elementDifference
								.getAttributeDifferences( ignoreApplier ) ) {
							final Element element = elementDifference.getElement();
							if ( !ignoreApplier.matches( element, attributeDifference ) ) {
								allDifferencesAlreadyListed = false;
								ignoreApplier.ignoreAttribute( element, attributeDifference );
							}
						}
					}
				}
			}
		}
		return allDifferencesAlreadyListed;
	}
}
