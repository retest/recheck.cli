package de.retest.recheck.cli;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.RestoreSystemProperties;
import org.junit.contrib.java.lang.system.SystemOutRule;

import de.retest.recheck.configuration.ProjectConfiguration;

public class PreConditionTest {

	@Rule
	public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();

	@Rule
	public RestoreSystemProperties restoreSystemProperties = new RestoreSystemProperties();

	@Test
	public void should_warn_when_not_a_recheck_project() throws Exception {
		System.setProperty( ProjectConfiguration.RETEST_PROJECT_ROOT, "/" );
		assertThat( PreCondition.isSatisfied() ).isFalse();
		assertThat( systemOutRule.getLog() ).contains( "Not a recheck project." );
	}

}
