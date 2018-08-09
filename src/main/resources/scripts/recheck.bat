@ECHO OFF

SET SCRIPT_DIR=%~dp0
SET JAR=%SCRIPT_DIR%\..\lib\recheck-cli.jar

SET JAVA=java

%JAVA% -jar "%JAR%" %*