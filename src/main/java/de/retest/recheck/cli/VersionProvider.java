package de.retest.recheck.cli;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import picocli.CommandLine.IVersionProvider;

public class VersionProvider implements IVersionProvider {

	private static final Logger logger = LoggerFactory.getLogger( VersionProvider.class );

	@Override
	public String[] getVersion() {
		final String recheckLogo = getRecheckLogo();
		final String recheckVersion = "recheck CLI version " + getClass().getPackage().getImplementationVersion();
		final String javaVersion = "Java version " + Runtime.class.getPackage().getImplementationVersion();
		return new String[] { recheckLogo, recheckVersion, javaVersion };
	}

	private String getRecheckLogo() {
		try ( InputStream in = getClass().getResourceAsStream( "/recheck-logo.txt" ) ) {
			return new BufferedReader( new InputStreamReader( in, StandardCharsets.UTF_8 ) ).lines() //
					.collect( Collectors.joining( System.lineSeparator() ) );
		} catch ( final IOException e ) {
			logger.error( "Couldn't read recheck logo.", e );
			return "";
		}
	}

}
