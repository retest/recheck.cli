package de.retest.recheck.cli.subcommands;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.RestoreSystemProperties;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.junit.rules.TemporaryFolder;

import de.retest.recheck.RecheckProperties;
import de.retest.recheck.cli.testutils.ProjectRootFaker;
import de.retest.recheck.cli.testutils.TestReportCreator;
import picocli.CommandLine;

public class ShowIT {

	@Rule
	public final TemporaryFolder temp = new TemporaryFolder();

	@Rule
	public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();

	@Rule
	public RestoreSystemProperties restoreSystemProperties = new RestoreSystemProperties();

	@Test
	public void show_without_argument_should_return_the_usage_message() {
		final String expected = "Usage: show [--exclude=<exclude>]... <testReport>\n" //
				+ "\nDescription:\n" //
				+ "Display differences of given test report.\n" //
				+ "\nParameters:\n" //
				+ "      <testReport>          Path to a test report file (.report extension). If\n"
				+ "                              the test report is not in the project directory,\n"
				+ "                              please specify the absolute path, otherwise a\n"
				+ "                              relative path is sufficient.\n" //
				+ "\nOptions:\n" //
				+ "      --exclude=<exclude>   Filter to exclude changes from the report. For a\n"
				+ "                              custom filter, please specify the absolute path.\n"
				+ "                              For predefined filters, a relative path is\n"
				+ "                              sufficient. Specify this option multiple times to\n"
				+ "                              use more than one filter.\n";
		assertThat( new CommandLine( new Show() ).getUsageMessage() ).isEqualToIgnoringNewLines( expected );
	}

	@Test
	public void show_should_print_differences() throws Exception {
		ProjectRootFaker.fakeProjectRoot( temp.getRoot().toPath() );
		final String[] args =
				{ TestReportCreator.createTestReportFileWithDiffs( temp, "diff_should_print_differences" ) };

		new CommandLine( new Show() ).execute( args );

		final String expected = "Suite 'diff_should_print_differences' has 3 difference(s) in 1 test(s):\n" //
				+ "\tTest 'test' has 3 difference(s) in 3 state(s):\n" //
				+ "\tcheck resulted in:\n" //
				+ "\t\tbaz (someTitle) at 'foo[1]/bar[1]/baz[1]':\n" //
				+ "\t\t\ttext:\n" //
				+ "\t\t\t  expected=\"original text\",\n" //
				+ "\t\t\t    actual=\"changed text\"\n" //
				+ "\tcheck resulted in:\n" //
				+ "\t\tbaz (someTitle) at 'foo[1]/bar[1]/baz[1]':\n" //
				+ "\t\t\twas deleted\n" //
				+ "\tcheck resulted in:\n" //
				+ "\t\tbaz (someTitle) at 'foo[1]/bar[1]/baz[1]':\n" //
				+ "\t\t\twas inserted";

		assertThat( systemOutRule.getLog() ).contains( expected );
	}

	@Test
	public void show_should_give_proper_error_message_when_given_test_report_is_not_a_test_report() throws Exception {
		final File notATestReport = temp.newFile();
		final String[] args = { notATestReport.getAbsolutePath() };

		new CommandLine( new Show() ).execute( args );

		assertThat( systemOutRule.getLog() ).contains(
				"The given file is not a test report. Please only pass files using the '.report' extension." );
	}

	@Test
	public void show_should_give_proper_error_message_when_given_test_report_does_not_exist() throws Exception {
		final File doesNotExist = new File( "/does/not/exist" + RecheckProperties.TEST_REPORT_FILE_EXTENSION );
		final String[] args = { doesNotExist.getAbsolutePath() };

		new CommandLine( new Show() ).execute( args );

		assertThat( systemOutRule.getLog() ).contains( "The given file report '" + doesNotExist.getAbsolutePath()
				+ "' does not exist. Please check the given file path." );
	}

	@Test
	public void show_should_print_used_filters_with_correct_exclude_options() throws IOException {
		ProjectRootFaker.fakeProjectRoot( temp.getRoot().toPath() );
		final String[] args = { "--exclude", "invisible-attributes.filter", "--exclude", "positioning.filter",
				TestReportCreator.createTestReportFileWithDiffs( temp ) };

		new CommandLine( new Show() ).execute( args );

		final String expected = "The following filter files have been applied:\n" //
				+ "\t/filter/web/positioning.filter\n" //
				+ "\t/filter/web/invisible-attributes.filter";

		assertThat( systemOutRule.getLog() ).contains( expected );
	}
}
