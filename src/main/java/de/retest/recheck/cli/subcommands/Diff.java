package de.retest.recheck.cli.subcommands;

import java.util.Arrays;
import java.util.List;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command( name = "diff", description = "Show all changes or all changes for a certain state" )
public class Diff implements Runnable {

	@Option( names = { "-h", "--help" }, usageHelp = true, hidden = true )
	private boolean displayHelp;

	@Parameters( description = "Checks to show the diff off. Leave blank to show the diff of all checks." )
	private List<String> checks;

	@Override
	public void run() {
		try {
			showDiffs();
		} catch ( final Exception e ) {
			System.out.println( "Exception showing diffs: " + e.getMessage() );
		}
	}

	public void showDiffs() {
		System.out.println("TODO Implement!");
		// TODO Implement showing the diff like when the tests fail
		// Configuration.ensureLoaded();
		// File latestReport = ReportFileUtils.getLatestReport();
		// DiffPrinter diffPrinter = new DiffPrinter( checks, System.out );
		// diffPrinter.printDiff( latestReport );
	}

	public static void main( String... args ) {
		Diff diff = new Diff();
		diff.checks = Arrays.asList( args );
		diff.showDiffs();
	}
}
