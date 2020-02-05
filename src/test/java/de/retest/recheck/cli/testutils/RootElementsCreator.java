package de.retest.recheck.cli.testutils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import de.retest.recheck.ui.Path;
import de.retest.recheck.ui.descriptors.Attribute;
import de.retest.recheck.ui.descriptors.Attributes;
import de.retest.recheck.ui.descriptors.IdentifyingAttributes;
import de.retest.recheck.ui.descriptors.PathAttribute;
import de.retest.recheck.ui.descriptors.RootElement;
import de.retest.recheck.ui.descriptors.StringAttribute;
import de.retest.recheck.ui.descriptors.TextAttribute;

public class RootElementsCreator {

	static List<RootElement> createRootElements( final boolean diff ) {
		final RootElement root = new RootElement( "someRetestId", new IdentifyingAttributes( getAttributes( diff ) ),
				new Attributes(), null, "someScreen", 0, "someTitle" );
		return Collections.singletonList( root );
	}

	static List<Attribute> getAttributes( final boolean diff ) {
		final Attribute path = new PathAttribute( Path.fromString( "foo[1]/bar[1]/baz[1]" ) );
		final Attribute string = new StringAttribute( "type", "baz" );
		final Attribute text = new TextAttribute( "text", diff ? "changed text" : "original text" );
		return Arrays.asList( path, string, text );
	}
}
