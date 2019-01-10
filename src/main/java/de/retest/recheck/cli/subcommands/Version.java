package de.retest.recheck.cli.subcommands;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command( name = "version", description = Version.VERSIOND_COMMAND_DESCRIPTION )
public class Version implements Runnable {

	public static final String VERSIOND_COMMAND_DESCRIPTION = "Display version info.";

	@Option( names = "--help", usageHelp = true, hidden = true )
	private boolean displayHelp;

	@Override
	public void run() {
		// TODO Implement.
		System.out.println( "Not yet implemented!" );
	}

}
