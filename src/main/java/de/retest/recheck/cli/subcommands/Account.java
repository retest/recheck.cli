package de.retest.recheck.cli.subcommands;

import de.retest.recheck.cli.subcommands.account.Login;
import de.retest.recheck.cli.subcommands.account.Logout;
import de.retest.recheck.cli.subcommands.account.Show;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command( name = "account", description = "Allows to log into your account and persist your token locally.",
		subcommands = { Login.class, Logout.class, Show.class } )
public class Account implements Runnable {

	@Option( names = "--help", usageHelp = true, hidden = true )
	private boolean displayHelp;

	@Override
	public void run() {
		CommandLine.usage( this, System.out );
	}

	boolean isDisplayHelp() {
		return displayHelp;
	}
}
