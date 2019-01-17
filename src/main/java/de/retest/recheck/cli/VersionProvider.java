package de.retest.recheck.cli;

import picocli.CommandLine.IVersionProvider;

public class VersionProvider implements IVersionProvider {

	@Override
	public String[] getVersion() throws RuntimeException {
		return new String[] { "recheck CLI version " + this.getClass().getPackage().getImplementationVersion(),
				"recheck Java version " +  Runtime.class.getPackage().getImplementationVersion() };
	}
}
