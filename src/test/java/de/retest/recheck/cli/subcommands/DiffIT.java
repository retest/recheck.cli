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
import de.retest.recheck.cli.testutils.GoldenMasterCreator;
import de.retest.recheck.cli.testutils.ProjectRootFaker;
import de.retest.recheck.cli.testutils.TestReportCreator;
import de.retest.recheck.util.FileUtil;
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
				"Usage: diff [--output=<directory>] [--exclude=<exclude>]...\n" + "            <goldenMasterPath>..." //
						+ "\nDescription:\n" //
						+ "Compare two Golden Masters.\n" //
						+ "\nParameters:\n" //
						+ "      <goldenMasterPath>...  Path to a golden master folder. This command may\n"
						+ "                               also be used with a single parameter to print\n"
						+ "                               out a test report (deprecated).\n" //
						+ "\nOptions:\n" //
						+ "      --exclude=<exclude>    Filter to exclude changes from the report. For a\n"
						+ "                               custom filter, please specify the absolute path.\n"
						+ "                               For predefined filters, a relative path is\n"
						+ "                               sufficient. Specify this option multiple times\n"
						+ "                               to use more than one filter.\n"
						+ "      --output=<directory>   Save differences of Golden Masters to specified\n"
						+ "                               directory as test report.\n";

		assertThat( new CommandLine( new Diff() ).getUsageMessage() ).isEqualToIgnoringNewLines( expected );
	}

	@Test
	public void diff_should_give_proper_error_message_when_given_golden_master_path_does_not_exist() throws Exception {
		temp.newFolder( "someotherpath" );
		temp.newFolder( "someotherpath", "goldenmaster" );

		final File outputDir = temp.newFolder( "somedirectory" );

		final File doesNotExist = temp.newFile( "doesnotexist" );
		final String[] args = { "--output", outputDir.getAbsolutePath(), doesNotExist.getAbsolutePath(), "does/exist" };

		new CommandLine( new Diff() ).execute( args );

		assertThat( systemOutRule.getLog() ).contains( "Could not load Golden Master from file '"
				+ FileUtil.canonicalFileQuietly( new File( doesNotExist, RecheckProperties.DEFAULT_XML_FILE_NAME ) )
				+ "'." );
	}

	@Test
	public void diff_should_provide_proper_error_message_when_provided_file_not_a_golden_master() throws IOException {
		temp.newFolder( "path" );
		temp.newFolder( "path", "notagoldenmaster" );
		temp.newFolder( "someotherpath" );
		temp.newFolder( "someotherpath", "goldenmaster" );

		final File notAGoldenMaster = temp.newFolder( "path", "notagoldenmaster", "a" );
		final File aGoldenMaster = temp.newFolder( "someotherpath", "goldenmaster", "b" );

		final File outputDir = temp.newFolder( "somedirectory" );

		GoldenMasterCreator.createErroneousGoldenMasterFile( notAGoldenMaster );
		GoldenMasterCreator.createGoldenMasterFile( aGoldenMaster, true );

		final File expectedPathErroneousFile =
				new File( FileUtil.canonicalFileQuietly( notAGoldenMaster ), RecheckProperties.DEFAULT_XML_FILE_NAME );

		final String[] args = { "--output", outputDir.getAbsolutePath(), notAGoldenMaster.getAbsolutePath(),
				aGoldenMaster.getAbsolutePath() };

		new CommandLine( new Diff() ).execute( args );

		assertThat( systemOutRule.getLog() ).containsSubsequence(
				"Could not load Golden Master from file '" + expectedPathErroneousFile.getAbsolutePath() + "'." );

	}

	@Test
	public void diff_should_print_differences_of_golden_masters() throws IOException {
		temp.newFolder( "path" );
		temp.newFolder( "path", "goldenmaster" );
		temp.newFolder( "someotherpath" );
		temp.newFolder( "someotherpath", "goldenmaster" );

		final File outputDir = temp.newFolder( "somedirectory" );

		final File gm1 = temp.newFolder( "path", "goldenmaster", "a" );
		final File gm2 = temp.newFolder( "someotherpath", "goldenmaster", "b" );

		GoldenMasterCreator.createGoldenMasterFile( gm1, true );
		GoldenMasterCreator.createGoldenMasterFile( gm2, false );

		final String[] args = { "--output", outputDir.getAbsolutePath(), gm1.getAbsolutePath(), gm2.getAbsolutePath() };

		new CommandLine( new Diff() ).execute( args );

		final String expected = "Comparison of Golden Masters resulted in:\n" // 
				+ "\tMetadata Differences:\n" // 
				+ "\t  Please note that these differences do not affect the result and are not included in the difference count.\n"
				+ "\t\tsome.driver:\n" // 
				+ "\t\t  expected=\"driverA\",\n" // 
				+ "\t\t    actual=\"driverB\"\n" // 
				+ "\tbaz (someTitle) at 'foo[1]/bar[1]/baz[1]':\n" // 
				+ "\t\twas deleted\n" // 
				+ "\tbaz (someTitle) at 'foo[1]/bar[1]/baz[1]':\n" // 
				+ "\t\twas inserted";

		assertThat( systemOutRule.getLog() ).containsSubsequence( expected );
	}

	@Test
	public void diff_should_not_persist_testreport_if_no_output_option() throws IOException {
		temp.newFolder( "path" );
		temp.newFolder( "path", "goldenmaster" );
		temp.newFolder( "someotherpath" );
		temp.newFolder( "someotherpath", "goldenmaster" );

		final File gm1 = temp.newFolder( "path", "goldenmaster", "a" );
		final File gm2 = temp.newFolder( "someotherpath", "goldenmaster", "b" );

		GoldenMasterCreator.createGoldenMasterFile( gm1, true );
		GoldenMasterCreator.createGoldenMasterFile( gm2, false );

		final String[] args = { gm1.getAbsolutePath(), gm2.getAbsolutePath() };

		new CommandLine( new Diff() ).execute( args );

		assertThat( systemOutRule.getLog() )
				.contains( "Overall, recheck found 2 difference(s) when checking 2 element(s)." );
	}

	@Test
	public void diff_should_persist_testreport_if_output_option() throws IOException {
		temp.newFolder( "path" );
		temp.newFolder( "path", "goldenmaster" );
		temp.newFolder( "someotherpath" );
		temp.newFolder( "someotherpath", "goldenmaster" );

		final File outputDir = temp.newFolder( "somedirectory" );

		final File gm1 = temp.newFolder( "path", "goldenmaster", "a" );
		final File gm2 = temp.newFolder( "someotherpath", "goldenmaster", "b" );

		GoldenMasterCreator.createGoldenMasterFile( gm1, true );
		GoldenMasterCreator.createGoldenMasterFile( gm2, false );

		final File outputFile = new File( outputDir, RecheckProperties.AGGREGATED_TEST_REPORT_FILE_NAME );

		final String[] args = { "--output", outputDir.getAbsolutePath(), gm1.getAbsolutePath(), gm2.getAbsolutePath() };

		new CommandLine( new Diff() ).execute( args );

		assertThat( outputFile ).exists();
	}

	@Test
	public void diff_should_apply_ignore_filter_with_exclude_option() throws IOException {
		temp.newFolder( "path" );
		temp.newFolder( "path", "goldenmaster" );
		temp.newFolder( "someotherpath" );
		temp.newFolder( "someotherpath", "goldenmaster" );

		final File outputDir = temp.newFolder( "somedirectory" );

		final File gm1 = temp.newFolder( "path", "goldenmaster", "a" );
		final File gm2 = temp.newFolder( "someotherpath", "goldenmaster", "b" );

		GoldenMasterCreator.createGoldenMasterFile( gm1, true );
		GoldenMasterCreator.createGoldenMasterFile( gm2, false );

		final File outputFile = new File( outputDir, RecheckProperties.AGGREGATED_TEST_REPORT_FILE_NAME );

		final String[] args = { gm1.getAbsolutePath(), gm2.getAbsolutePath(), "--output", outputDir.toString() };

		new CommandLine( new Diff() ).execute( args );

		assertThat( outputFile ).exists();
	}

	@Test
	public void diff_should_print_warning_when_exclude_option_with_invalid_filters() throws IOException {
		temp.newFolder( "path" );
		temp.newFolder( "path", "goldenmaster" );
		temp.newFolder( "someotherpath" );
		temp.newFolder( "someotherpath", "goldenmaster" );

		final File outputDir = temp.newFolder( "somedirectory" );

		final File gm1 = temp.newFolder( "path", "goldenmaster", "a" );
		final File gm2 = temp.newFolder( "someotherpath", "goldenmaster", "b" );

		GoldenMasterCreator.createGoldenMasterFile( gm1, true );
		GoldenMasterCreator.createGoldenMasterFile( gm2, false );

		final String[] args = { "--exclude", "sty-attributes.filter", "--exclude", "invisib.filter", "--output",
				outputDir.getAbsolutePath(), gm1.getAbsolutePath(), gm2.getAbsolutePath() };

		new CommandLine( new Diff() ).execute( args );

		assertThat( systemOutRule.getLog() )
				.contains( "The invalid filter files are: sty-attributes.filter, invisib.filter" );
	}

	@Test
	public void diff_should_print_used_filters_with_correct_exclude_options() throws IOException {
		temp.newFolder( "path" );
		temp.newFolder( "path", "goldenmaster" );
		temp.newFolder( "someotherpath" );
		temp.newFolder( "someotherpath", "goldenmaster" );

		final File outputDir = temp.newFolder( "somedirectory" );

		final File gm1 = temp.newFolder( "path", "goldenmaster", "a" );
		final File gm2 = temp.newFolder( "someotherpath", "goldenmaster", "b" );

		GoldenMasterCreator.createGoldenMasterFile( gm1, true );
		GoldenMasterCreator.createGoldenMasterFile( gm2, false );

		final String[] args = { "--exclude", "style-attributes.filter", "--output", outputDir.getAbsolutePath(),
				gm1.getAbsolutePath(), gm2.getAbsolutePath() };

		new CommandLine( new Diff() ).execute( args );

		final String expected = "The following filter files have been applied:\n" //
				+ "\t/filter/web/style-attributes.filter";

		assertThat( systemOutRule.getLog() ).contains( expected );
	}

	// remove this test in next version when old diff functionality is provided only under new command (see RET-1956)
	@Test
	public void diff_should_print_diff_of_test_report_if_used_with_single_parameter() throws IOException {
		ProjectRootFaker.fakeProjectRoot( temp.getRoot().toPath() );
		final String[] args =
				{ TestReportCreator.createTestReportFileWithDiffs( temp, "diff_should_print_differences" ) };

		new CommandLine( new Diff() ).execute( args );

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

	// remove this test in next version when old diff functionality is provided only under new command (see RET-1956)
	@Test
	public void diff_should_print_diff_of_test_report_if_used_with_single_parameter_and_exclude_option()
			throws IOException {
		ProjectRootFaker.fakeProjectRoot( temp.getRoot().toPath() );
		final String[] args = { "--exclude", "invisible-attributes.filter", "--exclude", "positioning.filter",
				TestReportCreator.createTestReportFileWithDiffs( temp ) };

		new CommandLine( new Diff() ).execute( args );

		final String expected = "\tTest 'test' has 3 difference(s) in 3 state(s):\n" //
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

}
