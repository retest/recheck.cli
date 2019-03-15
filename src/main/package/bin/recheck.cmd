@echo off

:: recheck.cli installation directory.
SET RECHECK_HOME=%~dp0\..

SET JAVA=java

SET JAVA_ARGS=-XX:+HeapDumpOnOutOfMemoryError
SET JAVA_ARGS=%JAVA_ARGS% -XX:-OmitStackTraceInFastThrow

%JAVA% %JAVA_ARGS% -jar "%RECHECK_HOME%\lib\recheck.cli.jar" %* 2>&1
