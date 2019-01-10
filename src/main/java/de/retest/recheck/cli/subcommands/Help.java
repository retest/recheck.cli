package de.retest.recheck.cli.subcommands;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command( name = "help", description = "Display help information about the specified command." )
public class Help implements Runnable {

	@Option( names = "--help", usageHelp = true, hidden = true )
	private boolean displayHelp;

	@Override
	public void run() {
		// TODO Implement.
		System.out.println( "Not yet implemented!" );
	}

}
