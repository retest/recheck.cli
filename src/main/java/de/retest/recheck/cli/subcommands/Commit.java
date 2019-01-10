package de.retest.recheck.cli.subcommands;

import java.io.File;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command( name = "commit", description = "Accept given differences." )
public class Commit implements Runnable {

	@Option( names = "--help", usageHelp = true, hidden = true )
	private boolean displayHelp;

	@Option( names = "--all", description = "Accept all differences from the given test report." )
	private boolean all;

	@Parameters( arity = "1", description = "Exactly one test report." )
	private File testReport;

	@Override
	public void run() {
		// TODO Implement.
		System.out.println( "Not yet implemented!" );
	}

}
