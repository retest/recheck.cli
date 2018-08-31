package de.retest.recheck.cli;

import de.retest.recheck.cli.subcommands.Commit;
import de.retest.recheck.cli.subcommands.Diff;
import de.retest.recheck.cli.subcommands.RetestGui;
import de.retest.recheck.cli.subcommands.Stage;
import de.retest.recheck.cli.subcommands.Status;
import de.retest.recheck.cli.subcommands.Unstage;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.IVersionProvider;
import picocli.CommandLine.Option;

/**
 * We are using git as a role model here. So we will have commands similar to https://git-scm.com/docs.
 *
 * @author roessler
 */
@Command( name = "recheck",
		subcommands = { Commit.class, Diff.class, Stage.class, Status.class, RetestGui.class, Unstage.class },
		description = "recheck - the Git for the GUI", versionProvider = RecheckCliMain.class )
public class RecheckCliMain implements IVersionProvider, Runnable {

	@Option( names = { "-h", "--help" }, usageHelp = true, description = "Display this help message" )
	private boolean displayHelp;

	@Option( names = { "-v", "--version" }, versionHelp = true, description = "Display version info" )
	private boolean displayVersion;

	@Override
	public String[] getVersion() throws Exception {
		// TODO read version from jar / pom / whatever
		//
		return new String[] { "retest version " + "0.2.0" };
	}

	@Override
	public void run() {
		System.out.println( "run recheck -h or recheck --help for more information" );
	}

	public static void main( final String[] args ) {
		CommandLine.run( new RecheckCliMain(), System.out, args );
	}

}
