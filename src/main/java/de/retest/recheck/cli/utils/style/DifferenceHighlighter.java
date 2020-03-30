package de.retest.recheck.cli.utils.style;

import de.retest.recheck.printer.highlighting.HighlightType;
import de.retest.recheck.printer.highlighting.Highlighter;
import de.retest.recheck.printer.highlighting.UnsupportedHighlightTypeException;
import picocli.CommandLine;

public class DifferenceHighlighter implements Highlighter {

	@Override
	public String highlight( final String msg, final HighlightType element ) {
		switch ( element ) {
			case KEY:
			case TYPE:
			case TESTREPLAYRESULT_NAME:
				return retestGreen( msg );
			case HEADING_SUITE_RESULTS:
				return underline( bold( retestRed( msg ) ) );
			case ELEMENT:
				return retestRed( msg );
			case HEADING_METADATA:
				return bold( retestGreen( msg ) );
			case VALUE_EXPECTED:
			case VALUE_ACTUAL:
			case ACTUAL:
			case EXPECTED:
				return msg;
			case NOTE:
			case PATH:
				return faint( msg );
			default:
				throw new UnsupportedHighlightTypeException( "The provided element is not defined for highlighting." );
		}
	}

	String retestGreen( final String msg ) {
		return CommandLine.Help.Ansi.AUTO.string( "@|fg(46) " + msg + "|@" );
	}

	String retestRed( final String msg ) {
		return CommandLine.Help.Ansi.AUTO.string( "@|fg(198) " + msg + "|@" );
	}

	String bold( final String msg ) {
		return CommandLine.Help.Ansi.AUTO.string( "@|bold " + msg + "|@" );
	}

	String faint( final String msg ) {
		return CommandLine.Help.Ansi.AUTO.string( "@|faint " + msg + "|@" );
	}

	String underline( final String msg ) {
		return CommandLine.Help.Ansi.AUTO.string( "@|underline " + msg + "|@" );
	}

}
