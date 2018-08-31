package de.retest.recheck.cli.subcommands;

import java.util.List;

//import de.retest.recheck.ChangeApplier;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command( name = "stage", description = "Stage all changes of a certain state and all same changes to other states" )
public class Stage implements Runnable {

	@Option( names = { "-h", "--help" }, usageHelp = true, hidden = true )
	private boolean displayHelp;

	@Parameters( description = "States to stage the changes off (blank stages all states)" )
	private List<String> states;

	@Override
	public void run() {
		try {
			stageChecks();
		} catch ( final Exception e ) {
			System.out.println( "Exception showing diffs: " + e.getMessage() );
		}
	}

	public void stageChecks() {
		System.out.println( "TODO Implement!" );
		// TODO Implement staging the given states
		// and persist that in a separate file
		// Configuration.ensureLoaded();
		// File latestReport = ReportFileUtils.getLatestReport();
		// ChangeApplier applier = new ChangeApplier(checks);
		// applier.apply(latestReport);
	}
}
