package de.retest.recheck.cli.testutils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.rules.TemporaryFolder;

import de.retest.recheck.Properties;
import de.retest.recheck.SuiteAggregator;
import de.retest.recheck.ignore.Filter;
import de.retest.recheck.persistence.RecheckSutState;
import de.retest.recheck.persistence.RecheckTestReportUtil;
import de.retest.recheck.report.ActionReplayResult;
import de.retest.recheck.report.SuiteReplayResult;
import de.retest.recheck.report.TestReplayResult;
import de.retest.recheck.report.action.ActionReplayData;
import de.retest.recheck.report.action.DifferenceRetriever;
import de.retest.recheck.report.action.WindowRetriever;
import de.retest.recheck.ui.DefaultValueFinder;
import de.retest.recheck.ui.Path;
import de.retest.recheck.ui.descriptors.Attribute;
import de.retest.recheck.ui.descriptors.Attributes;
import de.retest.recheck.ui.descriptors.IdentifyingAttributes;
import de.retest.recheck.ui.descriptors.PathAttribute;
import de.retest.recheck.ui.descriptors.RootElement;
import de.retest.recheck.ui.descriptors.StringAttribute;
import de.retest.recheck.ui.descriptors.SutState;
import de.retest.recheck.ui.descriptors.TextAttribute;
import de.retest.recheck.ui.diff.ElementIdentificationWarning;
import de.retest.recheck.ui.diff.RootElementDifference;
import de.retest.recheck.ui.diff.RootElementDifferenceFinder;

public class TestReportCreator {

	private final static String TEST_REPORT_FILENAME = "some" + Properties.TEST_REPORT_FILE_EXTENSION;

	public static String createTestReportFileWithoutDiffs( final TemporaryFolder folder ) throws IOException {
		final String uniqueSuiteName = "suiteWithoutDiffs-" + System.currentTimeMillis();
		final SuiteReplayResult suite = SuiteAggregator.getInstance().getSuite( uniqueSuiteName );
		return persistTestReport( folder, suite );
	}

	public static String createTestReportFileWithDiffs( final TemporaryFolder folder ) throws IOException {
		final SuiteReplayResult suite = createSuiteReplayResultWithDiffs( folder );
		return persistTestReport( folder, suite );
	}

	public static String createTestReportFileWithWarnings( final TemporaryFolder folder ) throws IOException {
		final SuiteReplayResult suite = createSuiteReplayResultWithDiffs( folder );

		final ElementIdentificationWarning elementIdentificationWarning =
				new ElementIdentificationWarning( "someElementIdentifier", "someTestClass", "someRetestId" );
		suite.getTestReplayResults().get( 0 ) //
				.getActionReplayResults().get( 0 ) //
				.getAllElementDifferences().get( 0 ) //
				.getAttributeDifferences( Filter.FILTER_NOTHING ).get( 0 ) //
				.setElementIdentificationWarning( elementIdentificationWarning );

		return persistTestReport( folder, suite );
	}

	private static String persistTestReport( final TemporaryFolder folder, final SuiteReplayResult suite )
			throws IOException {
		final File result = folder.newFile( TEST_REPORT_FILENAME );
		RecheckTestReportUtil.persist( suite, result );
		return result.getPath();
	}

	private static SuiteReplayResult createSuiteReplayResultWithDiffs( final TemporaryFolder folder )
			throws IOException {
		final String uniqueSuiteName = "suiteWithDiffs-" + System.currentTimeMillis();
		final SuiteReplayResult suite = SuiteAggregator.getInstance().getSuite( uniqueSuiteName );

		final TestReplayResult test = new TestReplayResult( "test", 0 );
		suite.addTest( test );

		final File goldenMaster = folder.newFolder( "goldenMaster" );
		final List<RootElement> rootElements = createRootElements( false );
		final List<RootElementDifference> rootElementDifferenceList = createRootElementDifferences( rootElements );
		RecheckSutState.createNew( goldenMaster, new SutState( rootElements ) );
		final DifferenceRetriever differenceRetriever = DifferenceRetriever.of( rootElementDifferenceList );

		final ActionReplayResult check =
				ActionReplayResult.withDifference( ActionReplayData.withoutTarget( "check", goldenMaster.getName() ),
						WindowRetriever.empty(), differenceRetriever, 0 );
		test.addAction( check );

		return suite;
	}

	private static List<RootElement> createRootElements( final boolean diff ) {
		final RootElement root = new RootElement( "someRetestId", new IdentifyingAttributes( getAttributes( diff ) ),
				new Attributes(), null, "someScreen", 0, "someTitle" );
		return Collections.singletonList( root );
	}

	private static List<RootElementDifference> createRootElementDifferences( final List<RootElement> rootElements ) {
		final DefaultValueFinder defaultFinder = ( identifyingAttributes, attributeKey, attributeValue ) -> false;
		final RootElementDifferenceFinder differenceFinder = new RootElementDifferenceFinder( defaultFinder );
		final RootElementDifference difference =
				differenceFinder.findDifference( rootElements.get( 0 ), createRootElements( true ).get( 0 ) );
		return Collections.singletonList( difference );
	}

	private static List<Attribute> getAttributes( final boolean diff ) {
		final Attribute a0 = new PathAttribute( Path.fromString( "foo[1]/bar[1]/baz[1]" ) );
		final Attribute a1 = new StringAttribute( "type", "baz" );
		final Attribute a2 = new TextAttribute( "text", diff ? "changed text" : "original text" );
		return Arrays.asList( a0, a1, a2 );
	}

}
