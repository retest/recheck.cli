package de.retest.recheck.cli;

import picocli.CommandLine.IVersionProvider;

public class VersionProvider implements IVersionProvider {

	@Override
	public String[] getVersion() {
		return new String[] { "recheck CLI version " + this.getClass().getPackage().getImplementationVersion(),
				"Java version " +  Runtime.class.getPackage().getImplementationVersion() };
	}
}
