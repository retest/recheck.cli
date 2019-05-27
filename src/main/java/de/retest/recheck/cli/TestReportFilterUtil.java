package de.retest.recheck.cli;

import static de.retest.recheck.ignore.RecheckIgnoreUtil.loadRecheckIgnore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import de.retest.recheck.ignore.CompoundFilter;
import de.retest.recheck.ignore.Filter;
import de.retest.recheck.ignore.SearchFilterFiles;

public class TestReportFilterUtil {

	public static Filter checkAndCollectFilterNames( final List<String> exclude ) throws IOException {
		if ( exclude == null ) {
			return Filter.FILTER_NOTHING;
		}
		final List<Filter> filters = new ArrayList<>();
		final Set<String> filterKeys = SearchFilterFiles.toFileNameFilterMapping().keySet();
		for ( final String filterName : exclude ) {
			for ( final String key : filterKeys ) {
				if ( filterName.equals( key ) ) {
					filters.add( SearchFilterFiles.toFileNameFilterMapping().get( key ) );
				}
			}
		}
		filters.add( loadRecheckIgnore() );
		return new CompoundFilter( filters );
	}
}
