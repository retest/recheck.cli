package de.retest.recheck.cli.subcommands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.retest.recheck.cli.VersionProvider;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command( name = "version", description = Version.VERSION_CMD_DESCRIPTION )
public class Version implements Runnable {

	public static final String VERSION_CMD_DESCRIPTION = "Display version info.";

	private static final Logger logger = LoggerFactory.getLogger( Version.class );

	@Option( names = "--help", usageHelp = true, hidden = true )
	private boolean displayHelp;

	@Override
	public void run() {
		final VersionProvider versionProvider = new VersionProvider();
		for ( final String s : versionProvider.getVersion() ) {
			logger.info( "{}", s );
		}
	}
}
