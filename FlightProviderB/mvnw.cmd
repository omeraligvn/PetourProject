@REM ----------------------------------------------------------------------------
@REM Maven Start Up Batch script
@REM ----------------------------------------------------------------------------

@echo off
@setlocal

set ERROR_CODE=0
set EXEC_DIR=%CD%

@REM Find the project base dir
set MAVEN_PROJECTBASEDIR=%CD%
:findBaseDir
IF EXIST "%MAVEN_PROJECTBASEDIR%\.mvn" goto baseDirFound
cd ..
IF "%MAVEN_PROJECTBASEDIR%"=="%CD%" goto baseDirNotFound
set MAVEN_PROJECTBASEDIR=%CD%
goto findBaseDir

:baseDirFound
cd "%EXEC_DIR%"
goto endDetectBaseDir

:baseDirNotFound
set MAVEN_PROJECTBASEDIR=%CD%
cd "%CD%"

:endDetectBaseDir

IF NOT EXIST "%JAVA_HOME%\bin\java.exe" (
  echo ERROR: JAVA_HOME is not set or points to invalid directory.
  exit /B 1
)

set WRAPPER_JAR="%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.jar"
set WRAPPER_LAUNCHER=org.apache.maven.wrapper.MavenWrapperMain

set DOWNLOAD_URL="https://repo.maven.apache.org/maven2/io/takari/maven-wrapper/0.5.6/maven-wrapper-0.5.6.jar"
FOR /F "tokens=1,2 delims==" %%A IN ("%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.properties") DO (
 IF "%%A"=="wrapperUrl" SET DOWNLOAD_URL=%%B
)

if not exist %WRAPPER_JAR% (
  powershell -Command "&{[Net.ServicePointManager]::SecurityProtocol=[Net.SecurityProtocolType]::Tls12; (New-Object Net.WebClient).DownloadFile('%DOWNLOAD_URL%', '%WRAPPER_JAR%')}"
)

"%JAVA_HOME%\bin\java.exe" -classpath %WRAPPER_JAR% "-Dmaven.multiModuleProjectDirectory=%MAVEN_PROJECTBASEDIR%" %WRAPPER_LAUNCHER% %*
if ERRORLEVEL 1 goto error
goto end

:error
set ERROR_CODE=1

:end
@endlocal & set ERROR_CODE=%ERROR_CODE%
exit /B %ERROR_CODE%
