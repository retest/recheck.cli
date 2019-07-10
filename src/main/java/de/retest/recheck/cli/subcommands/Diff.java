package de.retest.recheck.cli.subcommands;

import static de.retest.recheck.ignore.RecheckIgnoreUtil.loadRecheckIgnore;
import static de.retest.recheck.printer.DefaultValueFinderProvider.none;

import java.io.File;
import java.io.IOException;
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

	@Option( names = "--exclude", description = "Filter(s) to exclude changes from the diff." )
	private List<String> exclude;

	@Parameters( arity = "1", description = RecheckCli.REPORT_FILE_PARAM_DESCRIPTION )
	private File testReport;

	@Override
	public void run() {
		if ( !PreCondition.isSatisfied() ) {
			return;
		}
		try {
			final TestReport report = TestReportUtil.load( testReport );
			final Filter excludeFilter = FilterUtil.getExcludeFilterFiles( exclude );
			final TestReport filteredTestReport = TestReportFilter.filter( report, excludeFilter );
			final TestReportPrinter printer = new TestReportPrinter( none(), loadRecheckIgnore() );
			logger.info( "\n{}", printer.toString( filteredTestReport ) );
		} catch ( final TestReportFormatException e ) {
			logger.error( "The given file is not a test report. Please only pass files using the '{}' extension.",
					Properties.TEST_REPORT_FILE_EXTENSION );
		} catch ( final IOException e ) {
			logger.error( "Differences couldn't be printed:", e );
		} catch ( final KryoException e ) {
			logger.error( "The report was created with another, incompatible recheck version.\r\n"
					+ "Please, use the same recheck version to load a report with which it was generated." );
			logger.debug( "StackTrace: ", e );
		}
	}

	File getTestReport() {
		return testReport;
	}

}
