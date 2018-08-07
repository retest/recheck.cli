package de.retest.recheck.cli;

import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command( name = "recheck" )
public class RecheckCommand implements Runnable {

	@Override
	public void run() {
		CommandLine.usage( this, System.out );
	}
}
