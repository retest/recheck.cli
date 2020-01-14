package de.retest.recheck.cli.utils;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import de.retest.recheck.cli.VersionProvider;

class VersionProviderTest {

	@Test
	void versions_should_be_provided() throws Exception {
		final VersionProvider cut = new VersionProvider();

		final String[] versions = cut.getVersion();

		// logo + recheck.cli + recheck + Java
		assertThat( versions ).hasSize( 4 );

		final String recheckCliVersion = versions[1];
		final String recheckVersion = versions[2];
		final String javaVersion = versions[3];

		assertThat( recheckCliVersion ).startsWith( "recheck CLI version" );
		assertThat( recheckVersion ).startsWith( "recheck version" );
		assertThat( javaVersion ).startsWith( "Java version" );
	}

}
