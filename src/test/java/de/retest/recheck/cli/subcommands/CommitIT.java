package de.retest.recheck.cli.subcommands;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;

import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.RestoreSystemProperties;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.junit.rules.TemporaryFolder;

import de.retest.recheck.cli.util.TestReportCreator;
import de.retest.recheck.configuration.ProjectConfiguration;
import picocli.CommandLine;

public class CommitIT {

	@Rule
	public TemporaryFolder temp = new TemporaryFolder();

	@Rule
	public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();

	@Rule
	public final RestoreSystemProperties restoreSystemProperties = new RestoreSystemProperties();

	@Test
	public void commit_without_argument_should_return_the_usage_message() {
		final String expectedMessage = "Usage: commit [--all] [--exclude=<exclude>]... <testReport>\n"
				+ "Accept specified differences of given test report.\n"
				+ "      <testReport>          Path to a test report file (.report extension). If the\n"
				+ "                              test report is not in the project directory, please\n"
				+ "                              specify the absolute path, otherwise a relative path\n"
				+ "                              is sufficient."
				+ "      --all                 Accept all differences from the given test report.\n"
				+ "      --exclude=<exclude>   Filter(s) to exclude changes from the diff.\n";
		assertThat( new CommandLine( new Commit() ).getUsageMessage() ).isEqualToIgnoringNewLines( expectedMessage );
	}

	@Test
	public void commit_should_accept_all_passed_parameters() {
		final String[] args = { "--all", "\foo\bar", "--help" };
		final Commit cut = new Commit();
		new CommandLine( cut ).parseArgs( args );

		assertThat( cut.getTestReport().toString() ).isEqualTo( args[1] );
		assertThat( cut.isAll() ).isTrue();
		assertThat( cut.isDisplayHelp() ).isTrue();
	}

	@Test
	public void commit_should_not_accept_test_reports_without_differences() throws Exception {
		final File testReport = new File( TestReportCreator.createTestReportFileWithoutDiffs( temp ) );
		final String[] args = { "--all", testReport.getAbsolutePath() };
		final Commit cut = new Commit();
		new CommandLine( cut ).parseArgs( args );

		cut.run();

		assertThat( systemOutRule.getLog() ).contains( "The test report has no differences." );
	}

	@Test
	public void commit_should_accept_test_reports_with_differences_and_update_sut_state_files() throws Exception {
		System.setProperty( ProjectConfiguration.RETEST_PROJECT_ROOT, temp.getRoot().toString() );
		temp.newFolder( "src", "main", "java" );
		temp.newFolder( "src", "test", "java" );

		final File testReport = new File( TestReportCreator.createTestReportFileWithDiffs( temp ) );
		final String[] args = { "--all", testReport.getAbsolutePath() };
		final Commit cut = new Commit();
		new CommandLine( cut ).parseArgs( args );

		cut.run();

		assertThat( systemOutRule.getLog() ).contains( "Updated Golden Master" );
	}

	@Test
	public void commit_should_handle_sut_state_files_that_cannot_be_found() throws Exception {
		final File testReport = new File( TestReportCreator.createTestReportFileWithDiffs( temp ) );
		final String[] args = { "--all", testReport.getAbsolutePath() };
		final Commit cut = new Commit();
		new CommandLine( cut ).parseArgs( args );

		cut.run();

		final String expectedMessage = "The Golden Master 'suite_test_check' cannot be found.\n" //
				+ "Please make sure that the given test report '" + testReport.getAbsolutePath() //
				+ "' is within the corresponding project directory.\n";
		assertThat( systemOutRule.getLog() ).contains( expectedMessage );
	}
}
