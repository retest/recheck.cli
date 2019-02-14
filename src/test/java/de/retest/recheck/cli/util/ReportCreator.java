package de.retest.recheck.cli.util;

import java.io.File;
import java.io.IOException;

import org.junit.rules.TemporaryFolder;

import de.retest.recheck.LoadRecheckIgnoreUtil;
import de.retest.recheck.ReplayResultProvider;
import de.retest.recheck.persistence.RecheckReplayResultUtil;
import de.retest.recheck.report.SuiteReplayResult;

public class ReportCreator {

	public final static String REPORT_WITHOUT_DIFFS_NAME = "withoutDiffs.result";

	public static String createReportFileWithoutDiffs( final TemporaryFolder folder ) throws IOException {
		final File result = folder.newFile( REPORT_WITHOUT_DIFFS_NAME );
		final SuiteReplayResult suite = ReplayResultProvider.getInstance().getSuite( "suiteWithoutDiffs" );
		LoadRecheckIgnoreUtil.loadRecheckIgnore();
		RecheckReplayResultUtil.persist( suite, result );
		return result.getPath();
	}
}
