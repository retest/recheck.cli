package de.retest.recheck.cli.subcommands;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.retest.recheck.configuration.ProjectConfigurationUtil;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command( name = "ignore", description = "Ignore given differences." )
public class Ignore implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger( Ignore.class );

	private static final String RECHECK_IGNORE = "recheck.ignore";

	@Option( names = "--help", usageHelp = true, hidden = true )
	private boolean displayHelp;

	@Option( names = "--all", description = "Ignore all differences from the given test report." )
	private boolean all;

	@Option( names = "--list", description = "List all ignored elements." )
	private boolean list;

	@Parameters( arity = "0..1", description = "Exactly one test report." )
	private File testReport;

	@Override
	public void run() {
		if ( list ) {
			final Path projectConfig = ProjectConfigurationUtil.findProjectConfigurationFolder();
			final Path ignoreFile = projectConfig.resolve( RECHECK_IGNORE );

			if ( ignoreFile.toFile().exists() ) {
				logger.info( "These elements are ignored:" );
				try {
					Files.readAllLines( ignoreFile ).forEach( logger::info );
				} catch ( final IOException e ) {
					logger.error( "Error reading from recheck ignore file in {}.", ignoreFile );
				}
			}
		}
	}

}
