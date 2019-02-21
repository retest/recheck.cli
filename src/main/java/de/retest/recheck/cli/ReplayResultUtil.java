package de.retest.recheck.cli;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.retest.recheck.persistence.Persistence;
import de.retest.recheck.persistence.bin.KryoPersistence;
import de.retest.recheck.report.ReplayResult;

public class ReplayResultUtil {

	private static final Logger logger = LoggerFactory.getLogger( ReplayResultUtil.class );

	private ReplayResultUtil() {}

	public static ReplayResult load( final File replayResult ) throws IOException {
		logger.info( "Checking test report in path '{}'.", replayResult.getPath() );
		final Persistence<ReplayResult> resultPersistence = new KryoPersistence<>();
		return resultPersistence.load( replayResult.toURI() );
	}

	public static void print( final ReplayResult result, final File testReport ) {
		logger.info( "Test report '{}' has {} differences in {} tests.", testReport.getName(),
				result.getDifferencesCount(), result.getNumberOfTestsWithChanges() );
	}
}
