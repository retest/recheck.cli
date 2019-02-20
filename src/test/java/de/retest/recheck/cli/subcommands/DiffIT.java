package de.retest.recheck.cli.subcommands;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.junit.rules.TemporaryFolder;

import de.retest.recheck.cli.util.ReportCreator;
import picocli.CommandLine;
import picocli.CommandLine.ParseResult;

public class DiffIT {

	@Rule
	public final TemporaryFolder temp = new TemporaryFolder();

	@Rule
	public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();

	@Test
	public void diff_without_argument_should_return_the_usage_message() {
		final String expected = "Usage: diff <testReport>\n" //
				+ "Display given differences.\n" //
				+ "      <testReport>   Exactly one test report.\n";
		assertThat( new CommandLine( new Diff() ).getUsageMessage() ).isEqualTo( expected );
	}

	@Test
	public void diff_should_contain_passed_file() throws IOException {
		final File result = temp.newFile( "report.result" );
		final String[] args = { result.getPath() };
		final Diff cut = new Diff();
		final ParseResult cmd = new CommandLine( cut ).parseArgs( args );
		assertThat( cut.getTestReport() ).isEqualTo( result );
	}

	@Test
	public void diff_should_print_differences() throws Exception {
		final String[] args = { ReportCreator.createReportFileWithDiffs( temp ) };
		final Diff cut = new Diff();
		final ParseResult cmd = new CommandLine( cut ).parseArgs( args );
		cut.run();
		final String expected = "suite suite:\n" //
				+ "Test 'test' has 1 difference(s) (1 unique): \n" //
				+ "Check 'check' resulted in:\n" //
				+ "\telement [someText[]]:\n" //
				+ "\t at: foo[1]/bar[1]:\n" //
				+ "\t\texpected text: someText[] - actual text: someText[diff]\n";
		assertThat( systemOutRule.getLog().contains( expected ) ).isTrue();
	}
}
