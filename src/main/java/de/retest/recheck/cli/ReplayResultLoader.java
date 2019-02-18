package de.retest.recheck.cli;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.retest.recheck.configuration.Configuration;
import de.retest.recheck.persistence.Persistence;
import de.retest.recheck.persistence.bin.KryoPersistence;
import de.retest.recheck.report.ReplayResult;

public class ReplayResultLoader {

	private static final Logger logger = LoggerFactory.getLogger( ReplayResultLoader.class );

	private ReplayResultLoader() {
	}

	public static ReplayResult load( final File replayResult ) throws IOException {
		Configuration.ensureLoaded();
		logger.info( "Checking test report in path '{}'.", replayResult.getPath() );
		final Persistence<ReplayResult> resultPersistence = new KryoPersistence<>();
		return resultPersistence.load( replayResult.toURI() );
	}
}
