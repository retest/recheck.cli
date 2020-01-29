package de.retest.recheck.cli.testutils;

import static de.retest.recheck.cli.testutils.RootElementsCreator.createRootElements;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import de.retest.recheck.RecheckProperties;
import de.retest.recheck.persistence.GoldenMasterProviderImpl;
import de.retest.recheck.persistence.Persistence;
import de.retest.recheck.persistence.PersistenceFactory;
import de.retest.recheck.persistence.xml.util.StdXmlClassesProvider;
import de.retest.recheck.ui.descriptors.RootElement;
import de.retest.recheck.ui.descriptors.SutState;

public class GoldenMasterCreator {

	public static void createGoldenMasterFile( final File file, final boolean diff ) {
		final List<RootElement> set = createRootElements( diff );
		final SutState goldenMaster = new SutState( set, () -> createMetaData( diff ) );
		final GoldenMasterProviderImpl goldenMasterProvider =
				new GoldenMasterProviderImpl( createSutStatePersistence() );
		goldenMasterProvider
				.saveGoldenMaster( new File( file.getPath(), RecheckProperties.DEFAULT_XML_FILE_NAME ), goldenMaster );
	}

	public static void createErroneousGoldenMasterFile( final File file ) throws IOException {
		FileUtils.writeStringToFile( new File( file.getPath(), RecheckProperties.DEFAULT_XML_FILE_NAME ),
				"erroneousGoldenMaster", Charset.defaultCharset() );
	}

	private static Map<String, String> createMetaData( final boolean diff ) {
		final Map<String, String> metaData = new HashMap<>();
		metaData.put( "some.driver", diff ? "driverA" : "driverB" );
		return metaData;
	}

	private static Persistence<SutState> createSutStatePersistence() {
		return new PersistenceFactory( new HashSet<>( Arrays.asList( StdXmlClassesProvider.getXmlDataClasses() ) ) )
				.getPersistence();
	}

}
