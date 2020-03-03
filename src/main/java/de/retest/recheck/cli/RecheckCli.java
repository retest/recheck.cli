package de.retest.recheck.cli;

import static de.retest.recheck.cli.utils.HelpColorScheme.colorScheme;

import org.apache.commons.lang3.SystemUtils;
import org.fusesource.jansi.AnsiConsole;

import de.retest.recheck.cli.subcommands.Account;
import de.retest.recheck.cli.subcommands.Commit;
import de.retest.recheck.cli.subcommands.Completion;
import de.retest.recheck.cli.subcommands.Diff;
import de.retest.recheck.cli.subcommands.Ignore;
import de.retest.recheck.cli.subcommands.Show;
import de.retest.recheck.cli.subcommands.Version;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

// TODO Add Migrate command when https://github.com/retest/recheck.cli/issues/85#issuecomment-526102137 is addressed.
@Command( name = "recheck", //
		description = "Command-line interface for recheck.", //
		versionProvider = VersionProvider.class, //
		subcommands = { //
				Account.class, //
				CommandLine.HelpCommand.class, //
				Commit.class, //
				Completion.class, //
				Diff.class, //
				Ignore.class, //
				Show.class, //
				Version.class, //
		} )
public class RecheckCli implements Runnable {

	@Option( names = "--help", usageHelp = true, description = "Display this help message." )
	private boolean displayHelp;

	@Option( names = "--version", versionHelp = true, description = Version.VERSION_CMD_DESCRIPTION )
	private boolean displayVersion;

	@Override
	public void run() {
		CommandLine.usage( this, System.out, colorScheme );
	}

	public static void main( final String[] args ) {
		final int exitCode;

		if ( SystemUtils.IS_OS_WINDOWS ) {
			AnsiConsole.systemInstall(); // enable colors on Windows
			exitCode = createCommandLine( args );
			AnsiConsole.systemUninstall(); // clean-up
		} else {
			exitCode = createCommandLine( args );
		}
		System.exit( exitCode );
	}

	private static int createCommandLine( final String[] args ) {
		return new CommandLine( new RecheckCli() ) //
				.setUsageHelpAutoWidth( true ) //
				.setColorScheme( colorScheme ) //
				.execute( args );
	}
}
