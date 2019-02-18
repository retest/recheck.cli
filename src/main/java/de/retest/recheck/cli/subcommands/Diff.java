package de.retest.recheck.cli.subcommands;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.retest.recheck.cli.ReplayResultUtil;
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
		try {
			logger.info( "\n{}", ReplayResultUtil.load( testReport ) );
		} catch ( final IOException e ) {
			logger.error( "Differences couldn't be printed:", e );
		}
	}

	File getTestReport() {
		return testReport;
	}

}
