package de.retest.recheck.cli.subcommands;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.retest.recheck.RecheckProperties;
import de.retest.recheck.configuration.ProjectRootFinderUtil;
import de.retest.recheck.persistence.GoldenMasterProviderImpl;
import de.retest.recheck.persistence.NoGoldenMasterFoundException;
import de.retest.recheck.persistence.Persistence;
import de.retest.recheck.persistence.PersistenceFactory;
import de.retest.recheck.persistence.xml.util.StdXmlClassesProvider;
import de.retest.recheck.ui.descriptors.SutState;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command( name = "migrate", description = "Migrate Golden Master(s)." )
public class Migrate implements Runnable {

	private static final String RECHECK_FOLDER = "/src/test/resources/retest/recheck";
	private static final Logger logger = LoggerFactory.getLogger( Migrate.class );

	@Option( names = "--help", usageHelp = true, hidden = true )
	private boolean displayHelp;

	@Parameters( arity = "1", defaultValue = "", description = "The path of the Golden Master(s)." )
	private String goldenMasterPath;

	private final GoldenMasterProviderImpl goldenMasterProvider =
			new GoldenMasterProviderImpl( createSutStatePersistence() );

	@Override
	public void run() {
		try {
			migrateAllGoldenMasters( getAllGoldenMasters() );
		} catch ( final IOException e ) {
			logger.error( "The directory '{}' does not exist.", e.getMessage() );
		} catch ( final NoGoldenMasterFoundException e ) {
			logger.error( "{}", e.getMessage() );
		}
	}

	private void migrateAllGoldenMasters( final List<String> goldenMastersPaths ) throws NoGoldenMasterFoundException {
		if ( goldenMastersPaths.isEmpty() ) {
			throw new NoGoldenMasterFoundException( goldenMasterPath );
		}
		goldenMastersPaths.stream().forEach( path -> {
			final File goldenMaster = new File( path );
			final SutState sutState = goldenMasterProvider.loadGoldenMaster( goldenMaster );
			goldenMasterProvider.saveGoldenMaster( goldenMaster, sutState );
		} );
	}

	private List<String> getAllGoldenMasters() throws IOException {
		final String path = !goldenMasterPath.equals( "" ) ? goldenMasterPath : RECHECK_FOLDER;
		final File goldenMaster = ProjectRootFinderUtil.getProjectRoot()
				.map( gmPath -> Paths.get( gmPath.toAbsolutePath().toString(), path ) ).map( Path::toFile )
				.orElseThrow( IOException::new );
		try ( final Stream<Path> goldenMasterPaths = Files.walk( goldenMaster.toPath() ) ) {
			return goldenMasterPaths.filter( gmPath -> gmPath.endsWith( RecheckProperties.DEFAULT_XML_FILE_NAME ) )
					.map( gmPath -> gmPath.getParent().toString() ).collect( Collectors.toList() );
		}
	}

	private static Persistence<SutState> createSutStatePersistence() {
		return new PersistenceFactory( new HashSet<>( Arrays.asList( StdXmlClassesProvider.getXmlDataClasses() ) ) )
				.getPersistence();
	}

	boolean isDisplayHelp() {
		return displayHelp;
	}

}
