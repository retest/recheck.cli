package de.retest.recheck.cli.subcommands;

import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command( name = "status", description = "Show what states have changed and what is staged" )
public class Status implements Runnable {

	@CommandLine.Option( names = { "-h", "--help" }, usageHelp = true, hidden = true )
	private boolean displayHelp;

	@Override
	public void run() {
		try {
			showStatus();
		} catch ( final Exception e ) {
			System.out.println( "Exception showing diffs: " + e.getMessage() );
		}
	}

	public void showStatus() {
		System.out.println("TODO Implement!");
		// TODO Show which changes and stati have been staged,
		// and which are unstaged
		// Configuration.ensureLoaded();
		// File latestReport = ReportFileUtils.getLatestReport();
		// StatusPrinter statusPrinter = new StatusPrinter(System.out);
		// statusPrinter.printStatus(latestReport);
	}

	public static void main( String... args ) {
		Status status = new Status();
		status.showStatus();
	}
}
