package de.retest.recheck.cli.utils;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.retest.recheck.Properties;
import de.retest.recheck.cli.TestReportFormatException;
import de.retest.recheck.persistence.Persistence;
import de.retest.recheck.persistence.bin.KryoPersistence;
import de.retest.recheck.report.TestReport;

public class TestReportUtil {

	private static final Logger logger = LoggerFactory.getLogger( TestReportUtil.class );

	public static final String TEST_REPORT_PARAMETER_DESCRIPTION =
			"Path to a test report file (" + Properties.TEST_REPORT_FILE_EXTENSION + " extension). " //
					+ "If the test report is not in the project directory, please specify the absolute path, " //
					+ "otherwise a relative path is sufficient.";

	private TestReportUtil() {}

	public static TestReport load( final File testReportFile ) throws TestReportFormatException, IOException {
		logger.info( "Checking test report in path '{}'.", testReportFile.getPath() );
		checkExtension( testReportFile );
		final Persistence<TestReport> persistence = new KryoPersistence<>();
		return persistence.load( testReportFile.toURI() );
	}

	private static void checkExtension( final File testReportFile ) throws TestReportFormatException {
		if ( !testReportFile.getName().endsWith( Properties.TEST_REPORT_FILE_EXTENSION ) ) {
			throw new TestReportFormatException();
		}
	}

	public static void print( final TestReport testReport, final File testReportFile ) {
		logger.info( "Test report '{}' has {} differences in {} tests.", testReportFile.getName(),
				testReport.getDifferencesCount(), testReport.getNumberOfTestsWithChanges() );
	}
}
