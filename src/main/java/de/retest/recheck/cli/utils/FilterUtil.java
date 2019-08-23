package de.retest.recheck.cli.utils;

import java.util.ArrayList;
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
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FilterUtil {

	private FilterUtil() {}

	public static Filter getExcludeFilterFiles( final List<String> exclude ) {
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

	public static List<String> getInvalidFilters( final List<String> invalidFilters ) {
		final List<String> invalidFilterFiles = new ArrayList<>();
		if ( invalidFilters == null ) {
			return invalidFilterFiles;
		}
		for ( final String invalidFilterName : invalidFilters ) {
			if ( !SearchFilterFiles.toFileNameFilterMapping().containsKey( invalidFilterName ) ) {
				invalidFilterFiles.add( invalidFilterName );
			}
		}
		return invalidFilterFiles;
	}

	public static void logWarningForInvalidFilters( final List<String> invalidFilters ) {
		final String filter = invalidFilters.stream().collect( Collectors.joining( ", " ) );
		log.warn( "The invalid filter files are: {}", filter );
	}
}
