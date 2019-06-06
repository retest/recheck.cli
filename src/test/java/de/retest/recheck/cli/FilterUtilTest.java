package de.retest.recheck.cli;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import de.retest.recheck.ignore.CompoundFilter;
import de.retest.recheck.ignore.Filter;

class FilterUtilTest {

	@Test
	void when_exclude_is_not_empty_the_size_plus_GlobalIgnoreApplier_should_be_returned() throws IOException {
		// +1 for GlobalIgnoreApplier.
		final List<String> exclude = Arrays.asList( "positioning.filter" );
		final CompoundFilter filter = (CompoundFilter) FilterUtil.getExcludeFilterFiles( exclude );
		// +1 for GlobalIgnoreApplier.
		assertThat( filter.getFilters() ).hasSize( exclude.size() + 1 );
	}

	@Test
	void when_exclude_is_empty_only_GlobalIgnoreApplier_should_be_returned() throws IOException {
		// +1 for GlobalIgnoreApplier.
		final List<String> exclude = new ArrayList<>();
		final CompoundFilter filter = (CompoundFilter) FilterUtil.getExcludeFilterFiles( exclude );
		// +1 for GlobalIgnoreApplier.
		assertThat( filter.getFilters() ).hasSize( exclude.size() + 1 );
	}

	@Test
	void when_exclude_is_empty_filter_nothing_should_be_returned() throws Exception {
		final Filter filter = FilterUtil.getExcludeFilterFiles( null );
		assertThat( filter ).isSameAs( Filter.FILTER_NOTHING );
	}

}
