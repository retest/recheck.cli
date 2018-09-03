package de.retest.recheck.cli.subcommands;

import java.io.File;

import ch.qos.logback.core.Context;
import ch.qos.logback.core.util.StatusPrinter;
import de.retest.configuration.Configuration;
import de.retest.file.ReportFileUtils;
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
		// Show which changes and stati have been staged, which are unstaged
		Configuration.ensureLoaded();
		File latestReport = ReportFileUtils.getLatestReport();
		StatusPrinter.print( ( Context ) latestReport );
	}
}
