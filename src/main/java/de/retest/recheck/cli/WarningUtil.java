package de.retest.recheck.cli;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.retest.recheck.ui.diff.AttributeDifference;
import de.retest.recheck.ui.diff.ElementIdentificationWarning;
import de.retest.recheck.ui.review.ReviewResult;

public class WarningUtil {

	private static final Logger logger = LoggerFactory.getLogger( WarningUtil.class );

	private WarningUtil() {}

	public static void logWarnings( final ReviewResult reviewResult ) {
		reviewResult.getAllAttributeDifferences().stream() //
				.filter( WarningUtil::hasElementIdentificationWarning ) //
				.forEach( WarningUtil::logElementIdentificationWarning );
	}

	private static boolean hasElementIdentificationWarning( final AttributeDifference attributeDifference ) {
		return attributeDifference.getElementIdentificationWarning() != null;
	}

	private static void logElementIdentificationWarning( final AttributeDifference attributeDifference ) {
		final ElementIdentificationWarning warning = attributeDifference.getElementIdentificationWarning();

		if ( warning == null ) {
			return;
		}

		logger.warn( "*************** recheck warning ***************" );
		final String elementIdentifier = attributeDifference.getKey();
		final String expectedValue = attributeDifference.getExpectedToString();
		final String actualValue = attributeDifference.getActualToString();
		logger.warn( "The HTML attribute '{}' used for element identification changed from '{}' to '{}'.",
				elementIdentifier, expectedValue, actualValue );
		logger.warn( "recheck identified the element based on the persisted Golden Master." );
		final String testClassName = warning.getTestClassName();
		logger.warn( "If you apply these changes to the Golden Master, your test '{}' will break.", testClassName );
	}

}
