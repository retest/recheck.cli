package de.retest.recheck.cli.subcommands;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import de.retest.configuration.Configuration;
import de.retest.file.ReportFileUtils;
import de.retest.recheck.DiffPrinter;
import de.retest.recheck.LoadIgnoredComponentsUtil;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command( name = "diff", description = "Show all changes or all changes for a certain state" )
public class Diff implements Runnable {

	@Option( names = { "-h", "--help" }, usageHelp = true, hidden = true )
	private boolean displayHelp;

	@Parameters( description = "States to show the diff off (blank shows diffs of all states)" )
	private List<String> states;

	@Override
	public void run() {
		try {
			showDiffs();
		} catch ( final Exception e ) {
			System.out.println( "Exception showing diffs: " + e.getMessage() );
		}
	}

	public void showDiffs() throws FileNotFoundException {
		Configuration.ensureLoaded();
		LoadIgnoredComponentsUtil.loadIgnoredComponents();
		final File latestReport = ReportFileUtils.getLatestReport();
		final DiffPrinter diffPrinter = new DiffPrinter( states, System.out );
		diffPrinter.printDiff( latestReport );
	}
}
