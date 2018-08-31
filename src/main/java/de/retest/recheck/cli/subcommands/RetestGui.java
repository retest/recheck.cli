package de.retest.recheck.cli.subcommands;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command( name = "gui", description = "Open the retest GUI" )
public class RetestGui implements Runnable {

	@Option( names = { "-h", "--help" }, usageHelp = true, hidden = true )
	private boolean displayHelp;

	@Override
	public void run() {
		// TODO Find out if we have the GUI and run it
	}
}
