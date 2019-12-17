package de.retest.recheck.cli;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import org.apache.commons.lang3.SystemUtils;

import de.retest.recheck.cli.utils.Version;
import lombok.extern.slf4j.Slf4j;
import picocli.CommandLine.IVersionProvider;

@Slf4j
public class VersionProvider implements IVersionProvider {

	private static final String VERSION_FALLBACK = "n/a";

	@Override
	public String[] getVersion() {
		final String recheckLogo = getRecheckLogo();
		final String recheckCliVersion = getVersionString( "recheck CLI", Version.RECHECK_CLI_VERSION );
		final String recheckVersion = getVersionString( "recheck", Version.RECHECK_VERSION );
		final String javaVersion = getVersionString( "Java", getJavaInfo() );
		return new String[] { recheckLogo, recheckCliVersion, recheckVersion, javaVersion };
	}

	private String getVersionString( final String versionOf, final String version ) {
		final String nonNullVersion = version != null ? version : VERSION_FALLBACK;
		return String.format( "%s version %s", versionOf, nonNullVersion );
	}

	private String getRecheckLogoPath() {
		return SystemUtils.IS_OS_WINDOWS ? "/recheck-winlogo.txt" : "/recheck-logo.txt";
	}

	private String getRecheckLogo() {
		try ( InputStream in = getClass().getResourceAsStream( getRecheckLogoPath() ) ) {
			return new BufferedReader( new InputStreamReader( in, StandardCharsets.UTF_8 ) ).lines() //
					.collect( Collectors.joining( System.lineSeparator() ) );
		} catch ( final IOException e ) {
			log.error( "Couldn't read recheck logo.", e );
			return "";
		}
	}

	private String getJavaInfo() {
		return SystemUtils.JAVA_RUNTIME_NAME //
				+ " " + SystemUtils.JAVA_RUNTIME_VERSION //
				+ " (" + SystemUtils.JAVA_VENDOR + ")";
	}

}
