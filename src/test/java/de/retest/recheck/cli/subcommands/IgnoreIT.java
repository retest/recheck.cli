package de.retest.recheck.cli.subcommands;

import static de.retest.recheck.cli.testutils.ProjectRootFaker.fakeProjectRoot;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;

import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.RestoreSystemProperties;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.junit.rules.TemporaryFolder;

import de.retest.recheck.RecheckProperties;
import de.retest.recheck.cli.testutils.TestReportCreator;
import de.retest.recheck.configuration.ProjectConfiguration;
import picocli.CommandLine;

public class IgnoreIT {

	@Rule
	public final TemporaryFolder temp = new TemporaryFolder();

	@Rule
	public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();

	@Rule
	public RestoreSystemProperties restoreSystemProperties = new RestoreSystemProperties();

	@Test
	public void ignore_without_argument_should_return_the_usage_message() {
		final String expected = "Usage: ignore [--all] [--list] [<testReport>]\n" //
				+ "\nDescription:\n" //
				+ "Ignore specified differences of given test report.\n" //
				+ "\nParameters:\n" //
				+ "      [<testReport>]   Path to a test report file (.report extension). If the\n"
				+ "                         test report is not in the project directory, please\n"
				+ "                         specify the absolute path, otherwise a relative path\n"
				+ "                         is sufficient.\n" + "\nOptions:\n"
				+ "      --all            Ignore all differences from the given test report.\n"
				+ "      --list           List all ignored elements.";
		assertThat( new CommandLine( new Ignore() ).getUsageMessage() ).isEqualToIgnoringNewLines( expected );
	}

	@Test
	public void ignore_with_incomplete_arguments_should_return_info_message() {
		final String warning1 = "Currently only the two commands 'ignore --all' and 'ignore --list' are implemented.";
		final String warning2 = "A command to ignore specific differences will be implemented shortly.";
		final String[] args = { "foo/bar" };

		new CommandLine( new Ignore() ).execute( args );

		assertThat( systemOutRule.getLog() ).contains( warning1, warning2 );
	}

	@Test
	public void ignore_with_incomplete_arguments_should_return_help_message() {
		final String expected = "Please specify exactly one test report to ignore all differences.";
		final String[] args = { "--all" };

		new CommandLine( new Ignore() ).execute( args );

		assertThat( systemOutRule.getLog() ).contains( expected );
	}

	@Test
	public void ignore_should_not_ignore_the_report_because_there_are_no_differences() throws Exception {
		final String expected = "The test report has no differences.";
		final String[] args = { "--all", TestReportCreator.createTestReportFileWithoutDiffs( temp ) };

		new CommandLine( new Ignore() ).execute( args );

		assertThat( systemOutRule.getLog() ).contains( expected );
	}

	@Test
	public void ignore_should_find_differences_and_update_the_recheck_ignore_file() throws Exception {
		System.setProperty( ProjectConfiguration.RETEST_PROJECT_ROOT, temp.getRoot().toString() );
		temp.newFolder( "src", "main", "java" );
		temp.newFolder( "src", "test", "java" );
		temp.newFolder( ".retest" );
		temp.newFile( "/.retest/recheck.ignore" );

		final String expected = "The recheck ignore file has been updated.";
		final String[] args = { "--all", TestReportCreator.createTestReportFileWithDiffs( temp ) };

		new CommandLine( new Ignore() ).execute( args );

		assertThat( systemOutRule.getLog() ).contains( expected );
	}

	@Test
	public void ignore_should_find_differences_but_not_update_the_recheck_ignore_file() throws Exception {
		fakeProjectRoot( temp.getRoot().toPath() );
		final String expected = "All differences in the given test report are already ignored.";
		final String[] args = { "--all", TestReportCreator.createTestReportFileWithDiffs( temp ) };
		final Ignore cut = new Ignore();
		new CommandLine( cut ).parseArgs( args );

		// double execution to fill the empty recheck.ignore file
		// to check if differences are written multiple times to the recheck.ignore file.
		cut.run();
		cut.run();

		assertThat( systemOutRule.getLog() ).contains( expected );
	}

	@Test
	public void ignore_should_give_proper_error_message_when_given_test_report_is_not_a_test_report() throws Exception {
		final File notATestReport = temp.newFile();
		final String[] args = { "--all", notATestReport.getAbsolutePath() };

		new CommandLine( new Ignore() ).execute( args );

		assertThat( systemOutRule.getLog() ).contains(
				"The given file is not a test report. Please only pass files using the '.report' extension." );
	}

	@Test
	public void ignore_should_give_proper_error_message_when_given_test_report_does_not_exist() throws Exception {
		final File doesNotExist = new File( "/does/not/exist" + RecheckProperties.TEST_REPORT_FILE_EXTENSION );
		final String[] args = { "--all", doesNotExist.getAbsolutePath() };

		new CommandLine( new Ignore() ).execute( args );

		assertThat( systemOutRule.getLog() ).contains( "The given file report '" + doesNotExist.getAbsolutePath()
				+ "' does not exist. Please check the given file path." );
	}
}
