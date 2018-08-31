package de.retest.recheck.cli.subcommands;

import java.util.List;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command( name = "unstage", description = "Unstage individual changes" )
public class Unstage implements Runnable {

	@Option( names = { "-h", "--help" }, usageHelp = true, hidden = true )
	private boolean displayHelp;

	@Parameters( description = "States to unstage (blank unstages all states)" )
	private List<String> states;

	@Override
	public void run() {
		try {
			unstageChanges();
		} catch ( final Exception e ) {
			System.out.println( "Exception unstaging changes: " + e.getMessage() );
		}
	}

	public void unstageChanges() {
		System.out.println( "TODO Implement!" );
		// TODO Implement unstaging the given states and checks
		// Configuration.ensureLoaded();
		// File latestReport = ReportFileUtils.getLatestReport();
		// StatusPrinter statusPrinter = new StatusPrinter(System.out);
		// statusPrinter.printStatus(latestReport);
	}
}
