package de.retest.recheck.cli.subcommands;

import static de.retest.recheck.ignore.RecheckIgnoreUtil.loadRecheckIgnore;
import static de.retest.recheck.printer.DefaultValueFinderProvider.none;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.retest.recheck.cli.PreCondition;
import de.retest.recheck.cli.ReplayResultUtil;
import de.retest.recheck.printer.ReplayResultPrinter;
import de.retest.recheck.report.ReplayResult;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command( name = "diff", description = "Display given differences." )
public class Diff implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger( Diff.class );

	@Option( names = "--help", usageHelp = true, hidden = true )
	private boolean displayHelp;

	@Parameters( arity = "1", description = "Exactly one test report." )
	private File testReport;

	@Override
	public void run() {
		if ( !PreCondition.isSatisfied() ) {
			return;
		}
		try {
			final ReplayResult load = ReplayResultUtil.load( testReport );
			String message = new ReplayResultPrinter( none(), loadRecheckIgnore() ).toString( load );
			logger.info( "\n{}", message );
		} catch ( final IOException e ) {
			logger.error( "Differences couldn't be printed:", e );
		}
	}

	File getTestReport() {
		return testReport;
	}

}
