package de.retest.recheck.cli.subcommands;

import static de.retest.recheck.cli.utils.HelpColorScheme.colorScheme;
import static picocli.CommandLine.usage;

import de.retest.recheck.cli.subcommands.account.Login;
import de.retest.recheck.cli.subcommands.account.Logout;
import de.retest.recheck.cli.subcommands.account.Show;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command( name = "account", //
		descriptionHeading = "%nDescription:%n", //
		parameterListHeading = "%nParameters:%n", //
		optionListHeading = "%nOptions:%n", // 
		description = "Allows to log into and out of your account and show your API key.", //
		subcommands = { //
				Login.class, //
				Logout.class, //
				Show.class //
		} )
public class Account implements Runnable {

	@Option( names = "--help", usageHelp = true, hidden = true )
	private boolean displayHelp;

	@Override
	public void run() {
		usage( this, System.out, colorScheme );
	}

	boolean isDisplayHelp() {
		return displayHelp;
	}
}
