package de.retest.recheck.cli.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import de.retest.recheck.configuration.ProjectConfiguration;

public class ProjectRootFaker {

	public static void fakeProjectRoot( final Path base ) throws IOException {
		final Path projectRoot = base.toAbsolutePath().resolve( ProjectConfiguration.RETEST_PROJECT_CONFIG_FOLDER );
		final Path projectConfig = projectRoot.resolve( ProjectConfiguration.RETEST_PROJECT_PROPERTIES );
		final Path projectIgnore = projectRoot.resolve( ProjectConfiguration.RECHECK_IGNORE );

		System.setProperty( ProjectConfiguration.RETEST_PROJECT_ROOT, projectRoot.toString() );
		Files.createDirectories( projectRoot );
		Files.createFile( projectConfig );
		Files.createFile( projectIgnore );
	}

}
