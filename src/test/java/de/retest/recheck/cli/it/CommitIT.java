package de.retest.recheck.cli.it;

import static de.retest.recheck.persistence.RecheckStateFileProviderImpl.RECHECK_PROJECT_ROOT;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.junit.rules.TemporaryFolder;

import de.retest.recheck.cli.subcommands.Commit;
import de.retest.recheck.cli.util.ReportCreator;
import picocli.CommandLine;
import picocli.CommandLine.ParseResult;

public class CommitIT {

	private final static String FILE_WITH_DIFFS =
			"src/test/resources/de.retest.recheck.cli/commit/report.with.diffs.result";
	private final static String GOLDEN_MASTER_CHROME =
			"src/test/resources/de.retest.recheck.cli/commit/goldenmaster.chrome.xml";
	private final static String GOLDEN_MASTER_FIREFOX =
			"src/test/resources/de.retest.recheck.cli/commit/goldenmaster.firefox.xml";
	private final static String SCREENSHOT_CHROME =
			"src/test/resources/de.retest.recheck.cli/commit/screenshot.chrome.png";
	private final static String SCREENSHOT_FIREFOX =
			"src/test/resources/de.retest.recheck.cli/commit/screenshot.firefox.png";

	@Rule
	public TemporaryFolder temp = new TemporaryFolder();

	@Rule
	public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();

	@Test
	public void commit_without_argument_should_return_the_usage_message() {
		final String expected = "Usage: commit [--all] <testReport>\nAccept given differences.\n"
				+ "      <testReport>   Exactly one test report. If the test report is not in the\n"
				+ "                       project directory, please specify the absolute path,\n"
				+ "                       otherwise a relative path is sufficient\n"
				+ "      --all          Accept all differences from the given test report.\n";
		assertThat( new CommandLine( new Commit() ).getUsageMessage() ).isEqualTo( expected );
	}

	@Test
	public void commit_should_accept_all_passed_parameters() {
		final String[] args = { "--all", "/foo/bar", "--help" };
		final Commit cut = new Commit();
		final ParseResult cmd = new CommandLine( cut ).parseArgs( args );
		assertThat( cut.getTestReport().toString() ).isEqualTo( args[1] );
		assertThat( cut.isAll() ).isTrue();
		assertThat( cut.isDisplayHelp() ).isTrue();
	}

	@Test
	public void commit_should_not_accept_the_report_because_there_are_no_differences() throws Exception {
		final File originalFile = new File( ReportCreator.createReportFileWithoutDiffs() );
		final File temporaryFile = temp.newFile( ReportCreator.REPORT_WITHOUT_DIFFS_NAME );
		FileUtils.copyFile( originalFile, temporaryFile );

		final String expected = "The test report has no differences.";
		final String[] args = { "--all", temporaryFile.getAbsolutePath() };
		final Commit cut = new Commit();
		final ParseResult cmd = new CommandLine( cut ).parseArgs( args );

		cut.run();
		assertThat( systemOutRule.getLog().contains( expected ) ).isTrue();
	}

	@Test
	public void commit_should_detect_differences_but_can_not_accept_them() throws Exception {
		final File originalFile = new File( FILE_WITH_DIFFS );
		final File temporaryFile = temp.newFile( "report.with.diffs.result" );
		FileUtils.copyFile( originalFile, temporaryFile );

		final String expected = "Test report 'report.with.diffs.result' has 206 differences in 2 tests.\n"
				+ "No state file with name 'src/test/resources/retest/recheck/de.retest.web.it.PageFrameIT/page-frame-ChromeDriver.open.recheck' found!";
		final String[] args = { "--all", temporaryFile.getAbsolutePath() };
		final Commit cut = new Commit();
		final ParseResult cmd = new CommandLine( cut ).parseArgs( args );

		cut.run();
		assertThat( systemOutRule.getLog().contains( expected ) ).isTrue();
	}

	@Test
	public void commit_should_accept_the_differences_and_update_the_golden_masters() throws Exception {
		final File originalFile = new File( FILE_WITH_DIFFS );
		final File originalGMChrome = new File( GOLDEN_MASTER_CHROME );
		final File originalGMFirefox = new File( GOLDEN_MASTER_FIREFOX );
		final File originalScreenshotChrome = new File( SCREENSHOT_CHROME );
		final File originalScreenshotFirefox = new File( SCREENSHOT_FIREFOX );
		final File temporaryFile = temp.newFile( "report.with.diffs.result" );

		System.setProperty( RECHECK_PROJECT_ROOT, temp.getRoot().getAbsolutePath() );

		temp.newFolder( "src", "test", "resources", "retest", "recheck", "de.retest.web.it.PageFrameIT",
				"page-frame-ChromeDriver.open.recheck", "Screenshot" );
		temp.newFolder( "src", "test", "resources", "retest", "recheck", "de.retest.web.it.PageFrameIT",
				"page-frame-FirefoxDriver.open.recheck", "Screenshot" );

		final File temporaryGMChrome = temp.newFile(
				"src/test/resources/retest/recheck/de.retest.web.it.PageFrameIT/page-frame-ChromeDriver.open.recheck/retest.xml" );
		final File temporaryGMFirefox = temp.newFile(
				"src/test/resources/retest/recheck/de.retest.web.it.PageFrameIT/page-frame-FirefoxDriver.open.recheck/retest.xml" );
		final File temporaryScreenshotChrome = temp.newFile(
				"src/test/resources/retest/recheck/de.retest.web.it.PageFrameIT/page-frame-ChromeDriver.open.recheck/Screenshot/window_8c9c5194bf4d34c249f1716ebe0125cb0fc2ba1b6c18217da10f7c59910c8dea.png" );
		final File temporaryScreenshotFirefox = temp.newFile(
				"src/test/resources/retest/recheck/de.retest.web.it.PageFrameIT/page-frame-FirefoxDriver.open.recheck/Screenshot/window_49b7f30cde6243d13b2839269a1b982d93fcb525bd36216c37c57093dc4b37e1.png" );

		FileUtils.copyFile( originalFile, temporaryFile );
		FileUtils.copyFile( originalGMChrome, temporaryGMChrome );
		FileUtils.copyFile( originalGMFirefox, temporaryGMFirefox );
		FileUtils.copyFile( originalScreenshotChrome, temporaryScreenshotChrome );
		FileUtils.copyFile( originalScreenshotFirefox, temporaryScreenshotFirefox );

		final String expected = "Updated SUT state file /private" + temp.getRoot().getAbsolutePath()
				+ "/src/test/resources/retest/recheck/de.retest.web.it.PageFrameIT/page-frame-ChromeDriver.open.recheck.\n"
				+ "Updated SUT state file /private" + temp.getRoot().getAbsolutePath()
				+ "/src/test/resources/retest/recheck/de.retest.web.it.PageFrameIT/page-frame-FirefoxDriver.open.recheck.\n";
		final String[] args = { "--all", temporaryFile.getAbsolutePath() };
		final Commit cut = new Commit();
		final ParseResult cmd = new CommandLine( cut ).parseArgs( args );

		cut.run();
		assertThat( systemOutRule.getLog().contains( expected ) ).isTrue();
	}
}
