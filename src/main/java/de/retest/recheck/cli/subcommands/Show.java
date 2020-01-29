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
import de.retest.recheck.printer.TestReportPrinter;
import de.retest.recheck.report.TestReport;
import de.retest.recheck.report.TestReportFilter;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command( name = "show", description = "Display differences of given test report." )
public class Show implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger( Show.class );

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
			if ( FilterUtil.hasValidExcludeOption( exclude ) ) {
				printDiff();
			}
		} catch ( final Exception e ) {
			ErrorHandler.handle( e );
		}
	}

	private void printDiff() throws TestReportFormatException, IOException {
		final TestReport report = TestReportUtil.load( testReport );
		final Filter excludeFilter = FilterUtil.getExcludeFilterFiles( exclude );
		final Filter recheckIgnore = FilterUtil.loadRecheckIgnore();
		final TestReportFilter filter = new TestReportFilter( new CompoundFilter( excludeFilter, recheckIgnore ) );
		final TestReport filteredTestReport = filter.filter( report );
		final TestReportPrinter printer = new TestReportPrinter( none() );

		logger.info( "\n{}", printer.toString( filteredTestReport ) );

		if ( filteredTestReport.getDifferencesCount() > 0 ) {
			logger.info( "\nOverall, recheck found {} difference(s) when checking {} element(s).",
					filteredTestReport.getDifferencesCount(), filteredTestReport.getCheckedUiElementsCount() );
		}
	}

}
