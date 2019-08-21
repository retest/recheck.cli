package de.retest.recheck.cli.utils;

import java.util.Scanner;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SystemInUtil {

	public static String readLine() {
		// Do not close System.in.
		@SuppressWarnings( "resource" )
		final Scanner scanner = new Scanner( System.in );
		return scanner.nextLine();
	}

	public static boolean yesOrNo() {
		final String input = readLine();
		if ( input.equalsIgnoreCase( "yes" ) || input.equalsIgnoreCase( "y" ) ) {
			return true;
		}
		if ( input.equalsIgnoreCase( "no" ) || input.equalsIgnoreCase( "n" ) ) {
			return false;
		}
		log.warn( "Invalid input, please only use (y)es or (n)o:" );
		return yesOrNo();
	}

}
