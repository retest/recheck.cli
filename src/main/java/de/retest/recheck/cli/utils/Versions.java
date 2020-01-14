package de.retest.recheck.cli.utils;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.Sources;

@Sources( "classpath:retest-version.properties" )
public interface Versions extends Config {

	@Key( "recheck-cli-version" )
	String recheckCliVersion();

	@Key( "recheck-version" )
	String recheckVersion();

}
