package de.retest.recheck.cli.subcommands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command( name = "help", description = "Display help information about the specified command." )
public class Help implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger( Help.class );

	@Option( names = "--help", usageHelp = true, hidden = true )
	private boolean displayHelp;

	@Override
	public void run() {
		// TODO Implement.
		logger.info( "Not yet implemented!" );
	}

}
