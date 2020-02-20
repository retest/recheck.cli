package de.retest.recheck.cli.subcommands.account;

import static picocli.CommandLine.ExitCode.OK;
import static picocli.CommandLine.ExitCode.SOFTWARE;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.retest.recheck.Rehub;
import picocli.CommandLine.Command;
import picocli.CommandLine.IExitCodeGenerator;

@Command( name = "logout", description = "Allows to log out of your account." )
public class Logout implements Runnable, IExitCodeGenerator {

	private static final Logger logger = LoggerFactory.getLogger( Logout.class );

	private int exitCode = OK;

	@Override
	public void run() {
		Rehub.init();
		if ( Rehub.getRecheckApiKey() != null ) {
			Rehub.logout();
		} else {
			logger.info( "You are currently not logged in." );
			exitCode = SOFTWARE;
		}
	}

	@Override public int getExitCode() {
		return exitCode;
	}
}
