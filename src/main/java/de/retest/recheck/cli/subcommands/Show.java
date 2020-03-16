package de.retest.recheck.cli.subcommands;

import static de.retest.recheck.printer.DefaultValueFinderProvider.none;
import static picocli.CommandLine.ExitCode.OK;
import static picocli.CommandLine.ExitCode.SOFTWARE;
import static picocli.CommandLine.ExitCode.USAGE;

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
import de.retest.recheck.ignore.Filter;
import de.retest.recheck.printer.TestReportPrinter;
import de.retest.recheck.report.TestReport;
import de.retest.recheck.report.TestReportFilter;
import picocli.CommandLine.Command;
import picocli.CommandLine.IExitCodeGenerator;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command( name = "show", //
		descriptionHeading = "%nDescription:%n", //
		parameterListHeading = "%nParameters:%n", //
		optionListHeading = "%nOptions:%n", // 
		description = "Display differences of given test report." )
public class Show implements Runnable, IExitCodeGenerator {

	private static final Logger logger = LoggerFactory.getLogger( Show.class );

	@Option( names = "--help", usageHelp = true, hidden = true )
	private boolean displayHelp;

	@Option( names = "--exclude", description = FilterUtil.EXCLUDE_OPTION_DESCRIPTION )
	private List<String> exclude;

	@Parameters( arity = "1", description = TestReportUtil.TEST_REPORT_PARAMETER_DESCRIPTION )
	private File testReport;

	private int exitCode = OK;

	@Override
	public void run() {
		if ( !PreCondition.isSatisfied() ) {
			return;
		}
		try {
			if ( FilterUtil.hasValidExcludeOption( exclude ) ) {
				printDiff();
			} else {
				logger.warn( "Please specify a valid filter with the exclude option." );
				exitCode = USAGE;
			}
		} catch ( final Exception e ) {
			exitCode = SOFTWARE;
			ErrorHandler.handle( e );
		}
	}

	@Override
	public int getExitCode() {
		return exitCode;
	}

	private void printDiff() throws TestReportFormatException, IOException {
		final TestReport report = TestReportUtil.load( testReport );
		final Filter filterFiles = FilterUtil.getFilterFiles( exclude );
		final TestReportFilter filter = new TestReportFilter( filterFiles );

		FilterUtil.printUsedFilterPaths( filterFiles );

		final TestReport filteredTestReport = filter.filter( report );
		final TestReportPrinter printer = new TestReportPrinter( none() );

		logger.info( "\n\n{}\n\n", printer.toString( filteredTestReport ) );

		if ( filteredTestReport.getDifferencesCount() > 0 ) {
			logger.warn( "Overall, recheck found {} difference(s) when checking {} element(s).",
					filteredTestReport.getDifferencesCount(), filteredTestReport.getCheckedUiElementsCount() );
		}
	}

}
