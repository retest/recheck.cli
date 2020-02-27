Changelog
=========

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/) and this project adheres to
[Semantic Versioning](https://semver.org/).

But we group the changes as follows:

* *Breaking Changes* when a change requires manual activity at update.
* *Bug Fixes* when we fix broken functionality.
* *New Features* for added functionality.
* *Improvements* for other changed parts.


Table of Contents
-----------------

[TOC]


[Unreleased]
------------

### Breaking Changes

### Bug Fixes

### New Features

* Add `account` sub commands which allow users to login/logout and display the local API key

* `diff` command: compare any two golden masters. The option `--output` offers the possibility to save 
the differences as test report in the specified directory, and the option `--exclude` to filter the differences.
This command may still be used to show results of a test report. But it is advised to use the new command `show` instead.

### Improvements

* Do not print stack trace when a report is in an incompatible version.
* Exit with a non-zero return code when a sub-command encounters a problem.
* Enable ANSI on Windows and apply custom color scheme to usage messages.
* Change logger pattern to include levels and colors.
* Add whitespace for difference print out, e.g. when using `recheck show` to look at test report results.

--------------------------------------------------------------------------------


[1.9.0] (2020-01-29)
--------------------

### Bug Fixes

* Properly differentiate between recheck and recheck.cli version when displaying `recheck --version`.


[1.8.0] (2019-12-13)
--------------------

### Bug Fixes

* Ignoring inserted and deleted elements now actually ignores them properly.


[1.7.0] (2019-11-21)
--------------------

### Improvements

* Display suite description for test report printing so that tests now can properly be identified with their parent suite.


[1.6.0] (2019-11-06)
--------------------

### Improvements

* Improve `diff` command with better messages and a summary at the end.


[1.5.0] (2019-09-23)
--------------------

### Bug Fixes

* Fix ignore filter not being used when no filter were specified with `--exclude`.

### Improvements

* Update logo on windows due to restricted encoding in terminals.
* Improve description of `--exclude` option.
* Add better error messages for non-existing files.


[1.4.0] (2019-08-21)
--------------------

Changes not tracked before...
