package de.retest.recheck.cli;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import de.retest.recheck.ignore.CompoundFilter;
import de.retest.recheck.ignore.Filter;

class TestReportFilterUtilTest {

	@Test
	void when_exclude_is_not_null_the_size_plus_one_should_be_returned() throws IOException {
		// +1 for GlobalIgnoreApplier.
		final List<String> exclude = Arrays.asList( "positioning.filter" );
		final CompoundFilter filter = (CompoundFilter) TestReportFilterUtil.checkAndCollectFilterNames( exclude );
		// +1 for GlobalIgnoreApplier.
		assertThat( filter.getFilters() ).hasSize( exclude.size() + 1 );
	}

	@Test
	void when_exclude_is_null_one_should_be_returned() throws IOException {
		// +1 for GlobalIgnoreApplier.
		final List<String> exclude = new ArrayList<>();
		final CompoundFilter filter = (CompoundFilter) TestReportFilterUtil.checkAndCollectFilterNames( exclude );
		// +1 for GlobalIgnoreApplier.
		assertThat( filter.getFilters() ).hasSize( exclude.size() + 1 );
	}

	@Test
	void when_exclude_is_null_filter_nothing_should_be_returned() throws Exception {
		final Filter filter = TestReportFilterUtil.checkAndCollectFilterNames( null );
		assertThat( filter ).isSameAs( Filter.FILTER_NOTHING );
	}

}
