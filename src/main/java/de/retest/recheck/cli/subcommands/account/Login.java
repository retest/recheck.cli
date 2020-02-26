package de.retest.recheck.cli.subcommands.account;

import de.retest.recheck.Rehub;
import picocli.CommandLine.Command;

@Command( name = "login", //
		descriptionHeading = "%nDescription:%n", //
		parameterListHeading = "%nParameters:%n", //
		optionListHeading = "%nOptions:%n", //
		description = "Allows to log into your account." )
public class Login implements Runnable {

	@Override
	public void run() {
		Rehub.init();
		Rehub.authenticate();
	}

}
