# <a href="https://retest.dev"><img src="https://assets.retest.org/retest/ci/logos/recheck-screen.svg" width="300"/></a>.cli

[![Build Status](https://travis-ci.com/retest/recheck.cli.svg?branch=master)](https://travis-ci.com/retest/recheck.cli)
[![license](https://img.shields.io/badge/license-AGPL-brightgreen.svg)](https://github.com/retest/recheck.cli/blob/master/LICENSE)
[![PRs welcome](https://img.shields.io/badge/PRs-welcome-ff69b4.svg)](https://github.com/retest/recheck.cli/issues?q=is%3Aissue+is%3Aopen+label%3A%22help+wanted%22)
[![code with hearth by retest](https://img.shields.io/badge/%3C%2F%3E%20with%20%E2%99%A5%20by-retest-C1D82F.svg)](https://retest.de/en/)

The command-line interface for recheck, based on Git commands and options.

As of now, `recheck` will give you the following help message:

```
$ recheck
Usage: recheck [--help] [--version] [COMMAND]
Command-line interface for recheck.
      --help      Display this help message.
      --version   Display version info.
Commands:
  version     Display version info.
  diff        Display given differences.
  commit      Accept given differences.
  ignore      Ignore given differences.
  completion  Generate and display auto completion script.
  help        Displays help information about the specified command.
```

## Download and Installation

You can download the most recent version from the [GitHub releases page](https://github.com/retest/recheck.cli/releases/). Afterwards, you have to include the CLI into your path to use it from your shell. On Unix-like systems, you can do this by adding the following snippet to your `.bash_profile` and/or `.bashrc`:

```
export PATH="${PATH}:/path/to/recheck.cli/bin/"
```

On Windows, you can add `/path/to/recheck.cli/bin/` to your path by following [this tutorial](https://java.com/en/download/help/path.xml).

## Enabling Shell Auto-Completion

You can obtain an auto-completion script for Bash and ZSH via the `completion` command.
Simply add the resulting output to your `.bash_profile` or `.bashrc`, for example:

```
$ echo "source <(recheck completion)" >> ~/.bash_profile
```

## License

This project is licensed under the [AGPL license](LICENSE).
