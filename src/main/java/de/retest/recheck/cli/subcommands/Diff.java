package de.retest.recheck.cli.subcommands;

import static de.retest.recheck.printer.DefaultValueFinderProvider.none;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.retest.recheck.cli.PreCondition;
import de.retest.recheck.cli.TestReportFormatException;
import de.retest.recheck.cli.utils.ErrorHandler;
import de.retest.recheck.cli.utils.FilterUtil;
import de.retest.recheck.cli.utils.TestReportUtil;
import de.retest.recheck.ignore.CompoundFilter;
import de.retest.recheck.ignore.Filter;
import de.retest.recheck.ignore.RecheckIgnoreUtil;
import de.retest.recheck.printer.TestReportPrinter;
import de.retest.recheck.report.TestReport;
import de.retest.recheck.report.TestReportFilter;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command( name = "diff", description = "Display differences of given test report." )
public class Diff implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger( Diff.class );

	@Option( names = "--help", usageHelp = true, hidden = true )
	private boolean displayHelp;

	@Option( names = "--exclude", description = FilterUtil.EXCLUDE_OPTION_DESCRIPTION )
	private List<String> exclude;

	@Parameters( arity = "1", description = TestReportUtil.TEST_REPORT_PARAMETER_DESCRIPTION )
	private File testReport;

	@Override
	public void run() {
		if ( !PreCondition.isSatisfied() ) {
			return;
		}
		try {
			final List<String> invalidFilters = FilterUtil.getInvalidFilters( exclude );
			if ( !invalidFilters.isEmpty() ) {
				FilterUtil.logWarningForInvalidFilters( invalidFilters );
			} else {
				printDiff();
			}
		} catch ( final Exception e ) {
			ErrorHandler.handle( e );
		}
	}

	private void printDiff() throws TestReportFormatException, IOException {
		final TestReport report = TestReportUtil.load( testReport );
		final Filter excludeFilter = FilterUtil.getExcludeFilterFiles( exclude );
		final Filter recheckIgnore = RecheckIgnoreUtil.loadRecheckIgnore();
		final TestReport filteredTestReport =
				TestReportFilter.filter( report, new CompoundFilter( excludeFilter, recheckIgnore ) );
		final TestReportPrinter printer = new TestReportPrinter( none() );
		logger.info( "\n{}", printer.toString( filteredTestReport ) );
		logger.info( "\nOverall, recheck found {} differences when checking {} elements.",
				filteredTestReport.getDifferencesCount(), filteredTestReport.getCheckedUiElementsCount() );
	}

	File getTestReport() {
		return testReport;
	}

}
