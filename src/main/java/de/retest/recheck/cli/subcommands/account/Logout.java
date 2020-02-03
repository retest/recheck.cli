package de.retest.recheck.cli.subcommands.account;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.retest.recheck.Rehub;
import picocli.CommandLine.Command;

@Command( name = "logout", description = "Allows to log out of your account." )
public class Logout implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger( Logout.class );

	@Override
	public void run() {
		Rehub.init();
		if ( Rehub.getRecheckApiKey() != null ) {
			Rehub.logout();
		} else {
			logger.info( "You are currently not logged in." );
		}
	}

}
