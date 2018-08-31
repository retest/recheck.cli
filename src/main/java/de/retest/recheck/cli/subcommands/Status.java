package de.retest.recheck.cli.subcommands;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command( name = "status", description = "Show what states have changed and what is staged" )
public class Status implements Runnable {

	@Option( names = { "-h", "--help" }, usageHelp = true, hidden = true )
	private boolean displayHelp;

	@Override
	public void run() {
		try {
			showStatus();
		} catch ( final Exception e ) {
			System.out.println( "Exception showing status: " + e.getMessage() );
		}
	}

	public void showStatus() {
		System.out.println( "TODO Implement!" );
		// TODO Show which changes and stati have been staged,
		// and which are unstaged
		// Configuration.ensureLoaded();
		// File latestReport = ReportFileUtils.getLatestReport();
		// StatusPrinter statusPrinter = new StatusPrinter(System.out);
		// statusPrinter.printStatus(latestReport);
	}
}
