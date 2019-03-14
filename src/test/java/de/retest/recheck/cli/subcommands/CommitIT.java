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
import picocli.CommandLine.ParseResult;

public class CommitIT {

	@Rule
	public TemporaryFolder temp = new TemporaryFolder();

	@Rule
	public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();

	@Rule
	public final RestoreSystemProperties restoreSystemProperties = new RestoreSystemProperties();

	@Test
	public void commit_without_argument_should_return_the_usage_message() {
		final String expected = "Usage: commit [--all] <testReport>\nAccept given differences.\n"
				+ "      <testReport>   Exactly one test report. If the test report is not in the\n"
				+ "                       project directory, please specify the absolute path,\n"
				+ "                       otherwise a relative path is sufficient.\n"
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
		final File result = new File( TestReportCreator.createTestReportFileWithoutDiffs( temp ) );

		final String expected = "The test report has no differences.";
		final String[] args = { "--all", result.getAbsolutePath() };
		final Commit cut = new Commit();
		final ParseResult cmd = new CommandLine( cut ).parseArgs( args );

		cut.run();
		assertThat( systemOutRule.getLog() ).contains( expected );
	}

	@Test
	public void commit_should_accept_and_update_the_sut_state_file() throws Exception {
		System.setProperty( ProjectConfiguration.RETEST_PROJECT_ROOT, temp.getRoot().toString() );
		temp.newFolder( "src", "main", "java" );
		temp.newFolder( "src", "test", "java" );
		final File resultFile = new File( TestReportCreator.createTestReportFileWithDiffs( temp ) );

		final String expected = "Updated SUT state file";
		final String[] args = { "--all", resultFile.getAbsolutePath() };
		final Commit cut = new Commit();
		final ParseResult cmd = new CommandLine( cut ).parseArgs( args );

		cut.run();
		assertThat( systemOutRule.getLog() ).contains( expected );
	}
}
