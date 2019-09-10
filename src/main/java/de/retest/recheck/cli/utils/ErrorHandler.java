package de.retest.recheck.cli.utils;

import java.io.IOException;
import java.nio.file.NoSuchFileException;

import com.esotericsoftware.kryo.KryoException;

import de.retest.recheck.Properties;
import de.retest.recheck.cli.TestReportFormatException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ErrorHandler {

	private ErrorHandler() {
	}

	public static void handle( final Exception e ) {
		if ( e instanceof TestReportFormatException ) {
			log.error( "The given file is not a test report. Please only pass files using the '{}' extension.",
					Properties.TEST_REPORT_FILE_EXTENSION );
			return;
		}
		if ( e instanceof NoSuchFileException ) {
			log.error( "The given file report '{}' does not exist. Please check the given file path.",
					((NoSuchFileException) e).getFile() );
			log.debug( "Stack trace:", e );
			return;
		}
		if ( e instanceof IOException ) {
			log.error( "An error occurred while loading the test report.", e );
		}
		if ( e instanceof KryoException ) {
			log.error( "The report was created with another, incompatible recheck version.\n"
					+ "Please use the same recheck version to load a report with which it was generated." );
			log.debug( "Stack trace:", e );
		}
		throw new RuntimeException( e );
	}

}
