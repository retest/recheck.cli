package de.retest.recheck.cli.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Version {
	private static final String RECHECK_VERSION_PROPERTY = "recheck-version";
	private static final String RECHECK_CLI_VERSION_PROPERTY = "recheck-cli-version";
	private static final String RETEST_VERSION_PROPERTIES_FILE = "/retest-version.properties";
	private static final String UNKNOWN_VERSION = "unknown";

	public static final String RECHECK_CLI_VERSION = getProperty( RECHECK_CLI_VERSION_PROPERTY );
	public static final String RECHECK_VERSION = getProperty( RECHECK_VERSION_PROPERTY );

	private Version() {}

	private static String getProperty( final String key ) {
		try ( final InputStream is = Version.class.getResourceAsStream( RETEST_VERSION_PROPERTIES_FILE ) ) {
			final Properties props = new Properties();
			props.load( is );
			return props.getProperty( key, UNKNOWN_VERSION );
		} catch ( final IOException e ) {
			log.error( "Error loading version file '{}'", RETEST_VERSION_PROPERTIES_FILE );
			return UNKNOWN_VERSION;
		}
	}

}
