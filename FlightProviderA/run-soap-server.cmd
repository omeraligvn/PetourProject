@echo off
if "%JAVA_HOME%"=="" set "JAVA_HOME=C:\Program Files\Java\jdk-17"
call mvnw.cmd compile exec:java
pause
