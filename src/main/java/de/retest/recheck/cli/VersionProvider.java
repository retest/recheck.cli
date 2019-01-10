package de.retest.recheck.cli;

import picocli.CommandLine.IVersionProvider;

public class VersionProvider implements IVersionProvider {

	@Override
	public String[] getVersion() throws Exception {
		return new String[] { "recheck CLI version " + getClass().getPackage().getImplementationVersion() };
	}

}
