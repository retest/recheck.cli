package de.retest.recheck.cli.subcommands;

import static de.retest.recheck.ignore.RecheckIgnoreUtil.loadRecheckIgnore;
import static de.retest.recheck.printer.DefaultValueFinderProvider.none;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.retest.recheck.cli.PreCondition;
import de.retest.recheck.cli.RecheckCli;
import de.retest.recheck.cli.TestReportUtil;
import de.retest.recheck.ignore.CompoundFilter;
import de.retest.recheck.ignore.Filter;
import de.retest.recheck.ignore.SearchFilterFiles;
import de.retest.recheck.printer.TestReportPrinter;
import de.retest.recheck.report.TestReport;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command( name = "diff", description = "Display differences of given test report." )
public class Diff implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger( Diff.class );

	@Option( names = "--help", usageHelp = true, hidden = true )
	private boolean displayHelp;

	@Option( names = "--exclude", description = "Ignore matching elements during accept." )
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
			final TestReportPrinter printer = new TestReportPrinter( none(), checkAndCollectFilterNames() );
			logger.info( "\n{}", printer.toString( report ) );
		} catch ( final IOException e ) {
			logger.error( "Differences couldn't be printed:", e );
		}
	}

	Filter checkAndCollectFilterNames() throws IOException {
		final List<Filter> filters = new ArrayList<>();
		for ( final String filterName : exclude ) {
			final Optional<Filter> filter = SearchFilterFiles.searchFilterByName( filterName );
			if ( filter.isPresent() ) {
				filters.add( filter.get() );
			} else {
				logger.info( "The filter file " + filterName + " does not exist." );
			}
		}
		filters.add( loadRecheckIgnore() );
		return new CompoundFilter( filters );
	}

	File getTestReport() {
		return testReport;
	}

	List<String> getExclude() {
		return Collections.unmodifiableList( exclude );
	}

}
