package de.retest.recheck.cli.subcommands;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.RestoreSystemProperties;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.junit.rules.TemporaryFolder;

import de.retest.recheck.cli.util.ReportCreator;
import de.retest.recheck.configuration.ProjectConfiguration;
import picocli.CommandLine;
import picocli.CommandLine.ParseResult;

public class IgnoreIT {

	@Rule
	public final TemporaryFolder temp = new TemporaryFolder();

	@Rule
	public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();

	@Before
	public void setUp() throws Exception {
		final RestoreSystemProperties restoreSystemProperties =
				new RestoreSystemProperties( ProjectConfiguration.RETEST_PROJECT_ROOT, "" );
	}

	@Test
	public void ignore_without_argument_should_return_the_usage_message() {
		final String expected = "Usage: ignore [--all] [--list] [<testReport>]\n" + "Ignore given differences.\n"
				+ "      [<testReport>]   Exactly one test report.\n"
				+ "      --all            Ignore all differences from the given test report.\n"
				+ "      --list           List all ignored elements.\n";
		assertThat( new CommandLine( new Ignore() ).getUsageMessage() ).isEqualTo( expected );
	}

	@Test
	public void ignore_with_incomplete_arguments_should_return_info_message() {
		final String expected = "Currently only the two commands 'ignore --all' and 'ignore --list' are implemented.\n"
				+ "A command to ignore specific differences will be implemented shortly.\n";
		final String[] args = { "foo/bar" };
		final Ignore cut = new Ignore();
		final ParseResult cmd = new CommandLine( cut ).parseArgs( args );

		cut.run();
		assertThat( systemOutRule.getLog() ).isEqualTo( expected );
	}

	@Test
	public void ignore_with_incomplete_arguments_should_return_help_message() {
		final String expected = "Please specify exactly one test report to ignore all differences.\n";
		final String[] args = { "--all" };
		final Ignore cut = new Ignore();
		final ParseResult cmd = new CommandLine( cut ).parseArgs( args );

		cut.run();
		assertThat( systemOutRule.getLog() ).isEqualTo( expected );
	}

	@Test
	public void ignore_should_not_ignore_the_report_because_there_are_no_differences() throws Exception {
		final String expected = "The test report has no differences.";
		final String[] args = { "--all", ReportCreator.createReportFileWithoutDiffs( temp ) };
		final Ignore cut = new Ignore();
		final ParseResult cmd = new CommandLine( cut ).parseArgs( args );

		cut.run();
		assertThat( systemOutRule.getLog().contains( expected ) ).isTrue();
	}

	@Test
	public void ignore_should_find_differences_and_update_the_recheck_ignore_file() throws Exception {
		System.setProperty( ProjectConfiguration.RETEST_PROJECT_ROOT, temp.getRoot().getAbsolutePath() );
		temp.newFolder( "src", "main", "java" );
		temp.newFolder( "src", "test", "java" );
		temp.newFolder( ".retest" );

		temp.newFile( "/.retest/recheck.ignore" );

		final String expected = "The recheck ignore file has been updated.";
		final String[] args = { "--all", ReportCreator.createReportFileWithDiffs( temp ) };
		final Ignore cut = new Ignore();
		final ParseResult cmd = new CommandLine( cut ).parseArgs( args );

		cut.run();
		assertThat( systemOutRule.getLog().contains( expected ) ).isTrue();
	}

	@Test
	public void ignore_should_find_differences_but_not_update_the_recheck_ignore_file() throws Exception {
		System.setProperty( ProjectConfiguration.RETEST_PROJECT_ROOT, temp.getRoot().getAbsolutePath() );
		temp.newFolder( "src", "main", "java" );
		temp.newFolder( "src", "test", "java" );
		temp.newFolder( ".retest" );

		temp.newFile( "/.retest/recheck.ignore" );

		final String expected = "All differences in the given test report are already ignored.";
		final String[] args = { "--all", ReportCreator.createReportFileWithDiffs( temp ) };
		final Ignore cut = new Ignore();
		final ParseResult cmd = new CommandLine( cut ).parseArgs( args );

		// double execution to fill the empty recheck.ignore file
		// to check if differences are written multiple times to the recheck.ignore file.
		cut.run();
		cut.run();

		assertThat( systemOutRule.getLog().contains( expected ) ).isTrue();
	}
}
