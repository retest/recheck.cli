package de.retest.recheck.cli.utils;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.File;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import de.retest.recheck.Properties;
import de.retest.recheck.cli.TestReportFormatException;
import de.retest.recheck.cli.testutils.TestReportCreator;
import de.retest.recheck.cli.utils.TestReportUtil;

public class TestReportUtilTest {

	@Rule
	public TemporaryFolder temp = new TemporaryFolder();

	@Test
	public void should_load_test_report() throws Exception {
		final File testReportFile = new File( TestReportCreator.createTestReportFileWithoutDiffs( temp ) );
		assertThatCode( () -> TestReportUtil.load( testReportFile ) ).doesNotThrowAnyException();
	}

	@Test
	public void should_throw_exception_in_case_of_wrong_test_report_format() throws Exception {
		final File stateXml = temp.newFile( Properties.DEFAULT_XML_FILE_NAME );
		assertThatThrownBy( () -> TestReportUtil.load( stateXml ) )
				.isExactlyInstanceOf( TestReportFormatException.class );

		final File emptyFile = temp.newFile();
		assertThatThrownBy( () -> TestReportUtil.load( emptyFile ) )
				.isExactlyInstanceOf( TestReportFormatException.class );

		final File emptyFolder = temp.newFolder();
		assertThatThrownBy( () -> TestReportUtil.load( emptyFolder ) )
				.isExactlyInstanceOf( TestReportFormatException.class );
	}

}
