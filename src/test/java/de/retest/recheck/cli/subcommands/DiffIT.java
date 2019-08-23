package de.retest.recheck.cli.subcommands;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.RestoreSystemProperties;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.junit.rules.TemporaryFolder;

import de.retest.recheck.cli.testutils.ProjectRootFaker;
import de.retest.recheck.cli.testutils.TestReportCreator;
import picocli.CommandLine;

public class DiffIT {

	@Rule
	public final TemporaryFolder temp = new TemporaryFolder();

	@Rule
	public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();

	@Rule
	public RestoreSystemProperties restoreSystemProperties = new RestoreSystemProperties();

	@Test
	public void diff_without_argument_should_return_the_usage_message() {
		final String expected =
				"Usage: diff [--exclude=<exclude>]... <testReport>\n" + "Display differences of given test report.\n"
						+ "      <testReport>          Path to a test report file (.report extension). If\n"
						+ "                              the test report is not in the project directory,\n"
						+ "                              please specify the absolute path, otherwise a\n"
						+ "                              relative path is sufficient.\n"
						+ "      --exclude=<exclude>   Filter to exclude changes from the report. For a\n"
						+ "                              custom filter, please specify the absolute path.\n"
						+ "                              For predefined filters, a relative path is\n"
						+ "                              sufficient. Specify this option multiple times to\n"
						+ "                              use more than one filter.\n";
		assertThat( new CommandLine( new Diff() ).getUsageMessage() ).isEqualToIgnoringNewLines( expected );
	}

	@Test
	public void diff_should_contain_passed_file() throws IOException {
		final File result = temp.newFile( "test.report" );
		final String[] args = { result.getPath() };
		final Diff cut = new Diff();

		new CommandLine( cut ).parseArgs( args );

		assertThat( cut.getTestReport() ).isEqualTo( result );
	}

	@Test
	public void diff_should_print_differences() throws Exception {
		ProjectRootFaker.fakeProjectRoot( temp.getRoot().toPath() );
		final String[] args = { TestReportCreator.createTestReportFileWithDiffs( temp ) };
		final Diff cut = new Diff();
		new CommandLine( cut ).parseArgs( args );

		cut.run();
		final String expected = "Test 'test' has 1 difference(s) in 1 state(s):\n" //
				+ "check resulted in:\n" //
				+ "	baz [original text] at 'foo[1]/bar[1]/baz[1]':\n" //
				+ "		text: expected=\"original text\", actual=\"changed text\"";

		assertThat( systemOutRule.getLog() ).contains( expected );
	}

	@Test
	public void diff_should_give_proper_error_message_when_given_test_report_is_not_a_test_report() throws Exception {
		final File notATestReport = temp.newFile();
		final String[] args = { notATestReport.getAbsolutePath() };
		final Diff cut = new Diff();
		new CommandLine( cut ).parseArgs( args );

		cut.run();

		assertThat( systemOutRule.getLog() ).contains(
				"The given file is not a test report. Please only pass files using the '.report' extension." );
	}
}
