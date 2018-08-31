package de.retest.recheck.cli.subcommands;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command( name = "commit", description = "Apply all staged changes to their respective states" )
public class Commit implements Runnable {

	@Option( names = { "-h", "--help" }, usageHelp = true, hidden = true )
	private boolean displayHelp;

	@Override
	public void run() {
		// TODO Apply staged changes
	}
}
