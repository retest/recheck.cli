package de.retest.recheck.cli;

import java.io.File;
import java.io.IOException;

import de.retest.configuration.Configuration;
import de.retest.persistence.Persistence;
import de.retest.persistence.bin.KryoPersistence;
import de.retest.recheck.LoadRecheckIgnoreUtil;
import de.retest.report.ReplayResult;

public class ReplayResultLoader {

	private ReplayResultLoader() {}

	public static ReplayResult load( final File replayResult ) throws IOException {
		Configuration.ensureLoaded();
		LoadRecheckIgnoreUtil.loadRecheckIgnore();
		final Persistence<ReplayResult> resultPersistence = new KryoPersistence<>();
		return resultPersistence.load( replayResult.toURI() );
	}
}
