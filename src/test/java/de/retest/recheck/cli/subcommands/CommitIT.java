package de.retest.recheck.cli.subcommands;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;

import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.RestoreSystemProperties;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.junit.contrib.java.lang.system.TextFromStandardInputStream;
import org.junit.rules.TemporaryFolder;

import de.retest.recheck.cli.testutils.TestReportCreator;
import de.retest.recheck.configuration.ProjectConfiguration;
import picocli.CommandLine;

public class CommitIT {

	@Rule
	public TemporaryFolder temp = new TemporaryFolder();

	@Rule
	public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();

	@Rule
	public final TextFromStandardInputStream systemInMock = TextFromStandardInputStream.emptyStandardInputStream();

	@Rule
	public final RestoreSystemProperties restoreSystemProperties = new RestoreSystemProperties();

	@Test
	public void commit_without_argument_should_return_the_usage_message() {
		final String expectedMessage = "Usage: commit [--all] [--exclude=<exclude>]... <testReport>\n"
				+ "Accept specified differences of given test report.\n"
				+ "      <testReport>          Path to a test report file (.report extension). If\n"
				+ "                              the test report is not in the project directory,\n"
				+ "                              please specify the absolute path, otherwise a\n"
				+ "                              relative path is sufficient.\n"
				+ "      --all                 Accept all differences from the given test report.\n"
				+ "      --exclude=<exclude>   Filter to exclude changes from the diff. For a\n"
				+ "                              custom filter, please specify the absolute path.\n"
				+ "                              For predefined filters, a relative path is\n"
				+ "                              sufficient. Specify this option multiple times to\n"
				+ "                              use more than one filter.\n";
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

		final String expectedMessage = "The Golden Master 'goldenMaster' cannot be found.\n" //
				+ "Please make sure that the given test report '" + testReport.getAbsolutePath() //
				+ "' is within the corresponding project directory.\n";
		assertThat( systemOutRule.getLog() ).contains( expectedMessage );
	}

	@Test
	public void commit_should_give_proper_error_message_when_given_test_report_is_not_a_test_report() throws Exception {
		final File notATestReport = temp.newFile();
		final String[] args = { "--all", notATestReport.getAbsolutePath() };
		final Commit cut = new Commit();
		new CommandLine( cut ).parseArgs( args );

		cut.run();

		assertThat( systemOutRule.getLog() ).contains(
				"The given file is not a test report. Please only pass files using the '.report' extension." );
	}

	@Test
	public void commit_can_be_continued_after_element_identification_warning() throws Exception {
		systemInMock.provideLines( "y" );

		final File testReport = new File( TestReportCreator.createTestReportFileWithWarnings( temp ) );
		final String[] args = { "--all", testReport.getAbsolutePath() };
		final Commit cut = new Commit();
		new CommandLine( cut ).parseArgs( args );

		cut.run();

		final String warning =
				"The HTML attribute 'text' used for element identification changed from 'original text' to 'changed text'.\n" //
						+ "recheck identified the element based on the persisted Golden Master.\n" //
						+ "If you apply these changes to the Golden Master, your test 'someTestClass' will break.";
		final String question = "Are you sure you want to continue? (y)es or (n)o";
		final String response = "The Golden Master 'goldenMaster' cannot be found.";
		assertThat( systemOutRule.getLog() ).contains( warning, question, response );
	}

	@Test
	public void commit_can_be_aborted_after_element_identification_warning() throws Exception {
		systemInMock.provideLines( "n" );

		final File testReport = new File( TestReportCreator.createTestReportFileWithWarnings( temp ) );
		final String[] args = { "--all", testReport.getAbsolutePath() };
		final Commit cut = new Commit();
		new CommandLine( cut ).parseArgs( args );

		cut.run();

		final String warning =
				"The HTML attribute 'text' used for element identification changed from 'original text' to 'changed text'.\n" //
						+ "recheck identified the element based on the persisted Golden Master.\n" //
						+ "If you apply these changes to the Golden Master, your test 'someTestClass' will break.";
		final String question = "Are you sure you want to continue? (y)es or (n)o";
		final String response = "No changes are applied!";
		assertThat( systemOutRule.getLog() ).contains( warning, question, response );
	}

	@Test
	public void commit_should_handle_invalid_input_after_element_identification_warning() throws Exception {
		systemInMock.provideLines( "invalid", "n" );

		final File testReport = new File( TestReportCreator.createTestReportFileWithWarnings( temp ) );
		final String[] args = { "--all", testReport.getAbsolutePath() };
		final Commit cut = new Commit();
		new CommandLine( cut ).parseArgs( args );

		cut.run();

		final String response0 = "Invalid input, please only use (y)es or (n)o:";
		final String response1 = "No changes are applied!";
		assertThat( systemOutRule.getLog() ).contains( response0, response1 );
	}
}
