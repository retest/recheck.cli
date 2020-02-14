package de.retest.recheck.cli.subcommands.account;

import static picocli.CommandLine.ExitCode.OK;
import static picocli.CommandLine.ExitCode.SOFTWARE;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.retest.recheck.Rehub;
import picocli.CommandLine.Command;
import picocli.CommandLine.IExitCodeGenerator;

@Command( name = "show", //
		descriptionHeading = "%nDescription:%n", //
		parameterListHeading = "%nParameters:%n", //
		optionListHeading = "%nOptions:%n", //
		description = "Show your token e.g. for CI usage." )
public class Show implements Runnable, IExitCodeGenerator {

	private static final Logger logger = LoggerFactory.getLogger( Show.class );

	private int exitCode = OK;

	@Override
	public void run() {
		Rehub.init();
		final String apiKey = Rehub.getRecheckApiKey();
		if ( apiKey != null ) {
			logger.info( "This is your API key: {}", apiKey );
		} else {
			logger.info( "No API key found." );
			exitCode = SOFTWARE;
		}
	}

	@Override
	public int getExitCode() {
		return exitCode;
	}
}
