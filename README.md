# <a href="https://retest.dev"><img src="https://assets.retest.org/retest/ci/logos/recheck-screen.svg" width="300"/></a>.cli

[![Build project](https://github.com/retest/recheck.cli/actions/workflows/build-project.yml/badge.svg)](https://github.com/retest/recheck.cli/actions/workflows/build-project.yml)
![GitHub All Releases](https://img.shields.io/github/downloads/retest/recheck.cli/total.svg)
[![license](https://img.shields.io/badge/license-AGPL-brightgreen.svg)](https://github.com/retest/recheck.cli/blob/main/LICENSE)
[![PRs welcome](https://img.shields.io/badge/PRs-welcome-ff69b4.svg)](https://github.com/retest/recheck.cli/issues?q=is%3Aissue+is%3Aopen+label%3A%22help+wanted%22)
[![code with hearth by retest](https://img.shields.io/badge/%3C%2F%3E%20with%20%E2%99%A5%20by-retest-C1D82F.svg)](https://retest.de/)

***recheck.cli*** maintains your [recheck](https://github.com/retest/recheck) Golden Master files, Git-like on the command line.

As of now, `recheck` will give you the following help message:

```
$ recheck
recheck [--help] [--version] [COMMAND]
Command-line interface for recheck.
      --help      Display this help message.
      --version   Display version info.
Commands:
  account     Allows to log into and out of your account and show your API key.
  help        Displays help information about the specified command
  commit      Accept specified differences of given test report.
  completion  Generate and display an auto completion script.
  diff        Compare two Golden Masters.
  ignore      Ignore specified differences of given test report.
  show        Display differences of given test report.
  version     Display version info.
```

## Setup

Please refer to the [setup guide](https://retest.github.io/docs/recheck.cli/setup/) from our docs.

## License

This project is licensed under the [AGPL license](LICENSE).

