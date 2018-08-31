package de.retest.recheck.cli.subcommands;

import picocli.CommandLine;

@CommandLine.Command( name = "gui", description = "Open the retest GUI" )
public class RetestGui implements Runnable {

	@CommandLine.Option( names = { "-h", "--help" }, usageHelp = true, hidden = true )
	private boolean displayHelp;

	@Override
	public void run() {
		// TODO Find out if we have the GUI and run it
	}
}
