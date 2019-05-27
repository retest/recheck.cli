package de.retest.recheck.cli;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.retest.recheck.ignore.Filter;
import de.retest.recheck.report.TestReportFilter;
import de.retest.recheck.ui.descriptors.Element;
import de.retest.recheck.ui.diff.AttributeDifference;
import de.retest.recheck.ui.diff.AttributesDifference;

class TestReportFilterUtilTest {
	List<String> exclude;
	Filter filter;
	String keyToFilter;
	String keyNotToFilter;
	AttributeDifference filterMe;
	AttributeDifference notFilterMe;
	List<AttributeDifference> attributeDifferences;
	AttributesDifference attributesDifference;
	Element element;

	@BeforeEach
	void setUp() {
		keyToFilter = "outline";
		keyNotToFilter = "background-color";
		filterMe = new AttributeDifference( keyToFilter, null, null );
		notFilterMe = new AttributeDifference( keyNotToFilter, null, null );
		element = mock( Element.class );
		attributeDifferences = Arrays.asList( filterMe, notFilterMe );
		attributesDifference = mock( AttributesDifference.class );
		when( attributesDifference.getDifferences() ).thenReturn( attributeDifferences );

	}

	@Test
	void attributeDifference_should_be_filtered_after_using_checkAndCollectFilterNames() throws IOException {
		exclude = Arrays.asList( "positioning.filter" );
		final Filter filter = TestReportFilterUtil.checkAndCollectFilterNames( exclude );
		final AttributesDifference filteredAttributesDifference =
				TestReportFilter.filter( element, attributesDifference, filter );
		assertThat( filteredAttributesDifference.getDifferences() ).containsExactly( notFilterMe );

	}

	@Test
	void attributeDifference_should_not_be_filtered_using_checkAndCollectFilterNames() throws IOException {
		exclude = new ArrayList<>();
		filter = TestReportFilterUtil.checkAndCollectFilterNames( exclude );
		final AttributesDifference filteredAttributesDifference =
				TestReportFilter.filter( element, attributesDifference, filter );
		assertThat( filteredAttributesDifference.getDifferences() ).contains( filterMe );

	}

}
