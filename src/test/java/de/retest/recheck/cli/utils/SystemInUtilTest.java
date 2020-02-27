package de.retest.recheck.cli.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.NoSuchElementException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.junit.contrib.java.lang.system.TextFromStandardInputStream;

public class SystemInUtilTest {

	@Rule
	public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();

	@Rule
	public final TextFromStandardInputStream systemInMock = TextFromStandardInputStream.emptyStandardInputStream();

	@Test
	public void should_read_per_line() throws Exception {
		systemInMock.provideLines( "foo", "bar" );
		assertThat( SystemInUtil.readLine() ).isEqualTo( "foo" );
		assertThat( SystemInUtil.readLine() ).isEqualTo( "bar" );
		assertThatThrownBy( SystemInUtil::readLine ).isExactlyInstanceOf( NoSuchElementException.class );
	}

	@Test
	public void should_allow_yes_y_no_n() throws Exception {
		systemInMock.provideLines( "yes", "y", "no", "n" );
		assertThat( SystemInUtil.yesOrNo() ).isTrue();
		assertThat( SystemInUtil.yesOrNo() ).isTrue();
		assertThat( SystemInUtil.yesOrNo() ).isFalse();
		assertThat( SystemInUtil.yesOrNo() ).isFalse();
	}

	@Test
	public void should_repeatedly_ask_yes_or_no_when_input_is_invalid() throws Exception {
		systemInMock.provideLines( "invalid", "yes" );
		assertThat( SystemInUtil.yesOrNo() ).isTrue();
		assertThat( systemOutRule.getLog() ).endsWith( "Invalid input, please only use (y)es or (n)o:\n" );
	}

}
