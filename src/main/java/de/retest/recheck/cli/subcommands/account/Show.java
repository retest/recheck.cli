package de.retest.recheck.cli.subcommands.account;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.retest.recheck.Rehub;
import picocli.CommandLine.Command;

@Command( name = "show", description = "Show your token e.g. for CI usage." )
public class Show implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger( Show.class );

	@Override
	public void run() {
		Rehub.init();
		final String getofflineToken = Rehub.getRecheckApiKey();
		if ( getofflineToken != null ) {
			logger.info( "This is your API key: {}", getofflineToken );
		} else {
			logger.info( "No API key found." );
		}
	}

}
