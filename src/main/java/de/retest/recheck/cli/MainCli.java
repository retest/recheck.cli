package de.retest.recheck.cli;

import picocli.CommandLine;

public class MainCli {

	public static void main( final String[] args ) {
		CommandLine.run( new RecheckCommand(), args );
	}
}
