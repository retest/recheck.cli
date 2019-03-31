package de.retest.recheck.cli.subcommands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import picocli.AutoComplete;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Option;
import picocli.CommandLine.Spec;

@Command( name = "completion", description = "Generate and display an auto completion script." )
public class Completion implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger( Completion.class );

	@Option( names = "--help", usageHelp = true, hidden = true )
	private boolean displayHelp;

	@Spec
	protected CommandSpec spec;

	@Override
	public void run() {
		CommandLine root = spec.commandLine();
		while ( root.getParent() != null ) {
			root = root.getParent();
		}
		final String script = AutoComplete.bash( root.getCommandName(), root );
		if ( script.isEmpty() ) {
			logger.error( "Failed to generate the auto completion script." );
		} else {
			logger.info( script );
		}
	}

}
