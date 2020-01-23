package de.retest.recheck.cli.utils;

import java.util.List;

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
				.forEach( WarningUtil::logElementIdentificationWarnings );
	}

	private static boolean hasElementIdentificationWarning( final AttributeDifference attributeDifference ) {
		return attributeDifference.hasElementIdentificationWarning();
	}

	private static void logElementIdentificationWarnings( final AttributeDifference attributeDifference ) {
		final List<ElementIdentificationWarning> warnings = attributeDifference.getElementIdentificationWarnings();

		for ( final ElementIdentificationWarning warning : warnings ) {
			logger.warn( "********************************************************************************" );
			final String elementIdentifier = attributeDifference.getKey();
			final String expectedValue = attributeDifference.getExpectedToString();
			final String actualValue = attributeDifference.getActualToString();
			logger.warn( "The HTML attribute '{}' used for element identification changed from '{}' to '{}'.",
					elementIdentifier, expectedValue, actualValue );
			logger.warn( "recheck identified the element based on the persisted Golden Master." );
			final String testClassName = warning.getQualifiedTestName();
			logger.warn( "If you apply these changes to the Golden Master, your test '{}' will break.", testClassName );
			logger.warn( "********************************************************************************" );
		}
	}

}
