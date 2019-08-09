package de.retest.recheck.cli;

import java.util.Objects;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.retest.recheck.ui.diff.AttributeDifference;
import de.retest.recheck.ui.diff.ElementIdentificationWarning;
import de.retest.recheck.ui.review.ReviewResult;

public class WarningUtil {

	private static final Logger logger = LoggerFactory.getLogger( WarningUtil.class );

	private WarningUtil() {
	}

	public static void logWarnings( final ReviewResult reviewResult ) {
		reviewResult.getAllAttributeDifferences().stream() //
				.map( toWarningMessage() ) //
				.filter( Objects::nonNull ) //
				.forEach( logger::warn );
	}

	private static Function<AttributeDifference, String> toWarningMessage() {
		return attributeDifference -> {
			final ElementIdentificationWarning warning = attributeDifference.getElementIdentificationWarning();
			if ( warning == null ) {
				return null;
			}
			final String title = "*************** recheck warning ***************\n";
			final String elementIdentifier = attributeDifference.getKey();
			final String expectedValue = attributeDifference.getExpectedToString();
			final String actualValue = attributeDifference.getActualToString();
			final String elementIdentifierInfo =
					String.format( "The HTML %s attribute used for element identification changed from %s to %s.\n",
							elementIdentifier, expectedValue, actualValue );
			final String info = "recheck identified the element based on the persisted Golden Master.\n";
			final String testClassName = warning.getTestClassName();
			final String onApplyChangesInfo = //
					String.format( "If you apply these changes to the Golden Master, your test %s will break.",
							testClassName );
			return title + elementIdentifierInfo + info + onApplyChangesInfo;
		};
	}

}
