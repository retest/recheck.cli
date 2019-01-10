package de.retest.recheck.cli.subcommands;

import java.io.File;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command( name = "diff", description = "Display given differences." )
public class Diff implements Runnable {

	@Option( names = "--help", usageHelp = true, hidden = true )
	private boolean displayHelp;

	@Parameters( arity = "1", description = "Exactly one test report." )
	private File testReport;

	@Override
	public void run() {
		// TODO Implement.
		System.out.println( "Not yet implemented!" );
	}

}
