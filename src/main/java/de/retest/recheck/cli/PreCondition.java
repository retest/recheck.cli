package de.retest.recheck.cli;

import java.io.File;
import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.retest.recheck.configuration.ProjectRootFinderUtil;

public class PreCondition {

	private static final Logger logger = LoggerFactory.getLogger( PreCondition.class );

	private PreCondition() {}

	public static boolean isSatisfied() {
		final boolean condition = ProjectRootFinderUtil.getProjectRoot() //
				.map( Path::toFile ) //
				.map( File::exists ) //
				.orElse( false );
		if ( !condition ) {
			logger.warn( "Not a recheck project." );
		}
		return condition;
	}

}
