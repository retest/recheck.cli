package de.retest.recheck.cli.subcommands;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
		// TODO Implement.
		logger.info( "Not yet implemented!" );
	}

}
