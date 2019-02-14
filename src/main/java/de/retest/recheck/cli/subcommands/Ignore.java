package de.retest.recheck.cli.subcommands;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command( name = "ignore", description = "Ignore given differences." )
public class Ignore implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger( Ignore.class );

	@Option( names = "--help", usageHelp = true, hidden = true )
	private boolean displayHelp;

	@Option( names = "--all", description = "Ignore all differences from the given test report." )
	private boolean all;

	@Option( names = "--list", description = "List all ignored elements." )
	private boolean list;

	@Parameters( arity = "0..1", description = "Exactly one test report." )
	private File testReport;

	@Override
	public void run() {
		// TODO Implement.
		logger.info( "Not yet implemented!" );
	}

}
