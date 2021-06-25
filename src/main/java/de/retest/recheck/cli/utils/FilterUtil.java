package de.retest.recheck.cli.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.retest.recheck.ignore.CompoundFilter;
import de.retest.recheck.ignore.Filter;
import de.retest.recheck.ignore.PersistentFilter;
import de.retest.recheck.ignore.SearchFilterFiles;
import de.retest.recheck.review.counter.NopCounter;
import de.retest.recheck.review.workers.LoadFilterWorker;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FilterUtil {

	public static final String EXCLUDE_OPTION_DESCRIPTION = "Filter to exclude changes from the report. " //
			+ "For a custom filter, please specify the absolute path. " //
			+ "For predefined filters, a relative path is sufficient. " //
			+ "Specify this option multiple times to use more than one filter.";

	private static final Map<String, Filter> FILTER_MAPPING = SearchFilterFiles.toFileNameFilterMapping();

	private FilterUtil() {}

	public static Filter getFilterFiles( final List<String> exclude ) {
		if ( exclude == null ) {
			return loadRecheckIgnore();
		}
		final Set<String> excludeDistinct = new HashSet<>( exclude );
		final Stream<Filter> excluded = FILTER_MAPPING.entrySet().stream() //
				.filter( entry -> excludeDistinct.contains( entry.getKey() ) ) //
				.map( Entry::getValue );
		return Stream.concat( excluded, Stream.of( loadRecheckIgnore() ) ) //
				.collect( Collectors.collectingAndThen( Collectors.toList(), CompoundFilter::new ) );
	}

	public static List<String> getInvalidFilters( final List<String> invalidFilters ) {
		final List<String> invalidFilterFiles = new ArrayList<>();
		if ( invalidFilters == null ) {
			return invalidFilterFiles;
		}
		for ( final String invalidFilterName : invalidFilters ) {
			if ( !FILTER_MAPPING.containsKey( invalidFilterName ) ) {
				invalidFilterFiles.add( invalidFilterName );
			}
		}
		return invalidFilterFiles;
	}

	public static void logWarningForInvalidFilters( final List<String> invalidFilters ) {
		final String filter = String.join( ", ", invalidFilters );
		log.warn( "The invalid filter files are: {}", filter );
	}

	public static Filter loadRecheckIgnore() {
		final LoadFilterWorker worker = new LoadFilterWorker( NopCounter.getInstance() );
		try {
			return worker.load();
		} catch ( final IOException e ) {
			log.error( "Failed to load recheck.ignore.", e );
		}
		return Filter.NEVER_MATCH;
	}

	public static boolean hasValidExcludeOption( final List<String> exclude ) {
		final List<String> invalidFilters = getInvalidFilters( exclude );
		if ( !invalidFilters.isEmpty() ) {
			logWarningForInvalidFilters( invalidFilters );
			return false;
		}
		return true;
	}

	public static Stream<Filter> flattenRecursive( final Filter filter ) {
		return Stream.concat( Stream.of( filter ), getChilds( filter ).flatMap( FilterUtil::flattenRecursive ) );
	}

	private static Stream<Filter> getChilds( final Filter filter ) {
		if ( filter instanceof CompoundFilter ) {
			return ((CompoundFilter) filter).getFilters().stream();
		}
		if ( filter instanceof PersistentFilter ) {
			return Stream.of( ((PersistentFilter) filter).getFilter() );
		}
		return Stream.empty();
	}

	public static void printUsedFilterPaths( final Filter filterFiles ) {
		final String filterPathNames = flattenRecursive( filterFiles ) //
				.filter( PersistentFilter.class::isInstance ) //
				.map( PersistentFilter.class::cast ) //
				.map( PersistentFilter::getPath ) //
				.distinct() //
				.map( java.nio.file.Path::toAbsolutePath ) //
				.map( java.nio.file.Path::toString ) //
				.collect( Collectors.joining( "\n\t" ) );

		if ( filterPathNames.length() > 0 ) {
			log.info( "The following filter files have been applied:\n\t{}", filterPathNames );
		}
	}
}
