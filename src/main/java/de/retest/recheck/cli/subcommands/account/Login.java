package de.retest.recheck.cli.subcommands.account;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.retest.recheck.Rehub;
import picocli.CommandLine.Command;

@Command( name = "login", description = "Allows to log into your account" )
public class Login implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger( Login.class );

	@Override
	public void run() {
		Rehub.init();
		Rehub.authenticate();
	}

}
