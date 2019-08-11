package de.retest.recheck.cli;

import de.retest.recheck.cli.subcommands.Commit;
import de.retest.recheck.cli.subcommands.Completion;
import de.retest.recheck.cli.subcommands.Diff;
import de.retest.recheck.cli.subcommands.Ignore;
import de.retest.recheck.cli.subcommands.Version;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command( name = "recheck", description = "Command-line interface for recheck.",
		versionProvider = VersionProvider.class, subcommands = { Version.class, Diff.class, Commit.class, Ignore.class,
				Completion.class, CommandLine.HelpCommand.class } )
public class RecheckCli implements Runnable {

	@Option( names = "--help", usageHelp = true, description = "Display this help message." )
	private boolean displayHelp;

	@Option( names = "--version", versionHelp = true, description = Version.VERSION_CMD_DESCRIPTION )
	private boolean displayVersion;

	@Override
	public void run() {
		CommandLine.usage( this, System.out );
	}

	public static void main( final String[] args ) {
		final int exitCode = new CommandLine( new RecheckCli() ).execute( args );
		System.exit( exitCode );
	}

}
