# ![recheck logo](https://user-images.githubusercontent.com/1871610/41766965-b69d46a2-7608-11e8-97b4-c6b0f047d455.png) command-line interface (CLI)

[![Build Status](https://travis-ci.com/retest/recheck-cli.svg?branch=master)](https://travis-ci.com/retest/recheck-cli)
[![license](https://img.shields.io/badge/license-AGPL-brightgreen.svg)](https://github.com/retest/recheck-cli/blob/master/LICENSE)
[![PRs welcome](https://img.shields.io/badge/PRs-welcome-ff69b4.svg)](https://github.com/retest/recheck-cli/issues?q=is%3Aissue+is%3Aopen+label%3A%22help+wanted%22)
[![code with hearth by retest](https://img.shields.io/badge/%3C%2F%3E%20with%20%E2%99%A5%20by-retest-C1D82F.svg)](https://retest.de/en/)

The command-line interface for recheck, based on Git commands and options.

As of now, `recheck` will give you the following help message:

```
Usage: recheck [--help] [--version] [COMMAND]
Command-line interface for recheck.
      --help      Display this help message.
      --version   Display version info.
Commands:
  help         Display help information about the specified command.
  version      Display version info.
  diff         Display given differences.
  commit       Accept given differences.
  ignore       Ignore given differences.
  completion   Generate and display auto completion script.
```


## Download and Installation

The recheck CLI is still under development, but we will soon release a minimal working solution.

## Enabling Shell Auto-Completion

You can obtain an auto-completion script for Bash and ZSH via the `completion` command.
Simply add the resulting output to your `.bash_profile` or `.bashrc, for example:

```
Usage: $ echo "source <(recheck-cli_completion)" >> ~/.bashrc
```

## License

This project is licensed under the [AGPL license](LICENSE).
