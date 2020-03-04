package de.retest.recheck.cli.utils;

import picocli.CommandLine;

public class HelpColorScheme {

	private HelpColorScheme() {}

	public static final CommandLine.Help.ColorScheme colorScheme = new CommandLine.Help.ColorScheme.Builder() //
			.commands( CommandLine.Help.Ansi.Style.fg_green ) //
			.parameters( CommandLine.Help.Ansi.Style.fg_yellow ) //
			.build();

}
