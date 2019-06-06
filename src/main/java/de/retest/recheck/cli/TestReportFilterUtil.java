package de.retest.recheck.cli;

import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.retest.recheck.ignore.CompoundFilter;
import de.retest.recheck.ignore.Filter;
import de.retest.recheck.ignore.RecheckIgnoreUtil;
import de.retest.recheck.ignore.SearchFilterFiles;

public class TestReportFilterUtil {

	private TestReportFilterUtil() {
	}

	public static Filter checkAndCollectFilterNames( final List<String> exclude ) {
		if ( exclude == null ) {
			return Filter.FILTER_NOTHING;
		}
		final Set<String> excludeDistinct = new HashSet<>( exclude );
		final Stream<Filter> excluded = SearchFilterFiles.toFileNameFilterMapping().entrySet().stream() //
				.filter( entry -> excludeDistinct.contains( entry.getKey() ) ) //
				.map( Entry::getValue );
		return Stream.concat( excluded, Stream.of( RecheckIgnoreUtil.loadRecheckIgnore() ) ) //
				.collect( Collectors.collectingAndThen( Collectors.toList(), CompoundFilter::new ) );
	}
}
