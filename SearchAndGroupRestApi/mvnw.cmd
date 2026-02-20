@REM Maven Wrapper script for Windows
@echo off
setlocal enabledelayedexpansion

REM Try to find Java if JAVA_HOME is not set
if "%JAVA_HOME%" == "" (
    REM Check common Java installation paths
    if exist "C:\Program Files\Java\jdk-17" (
        set "JAVA_HOME=C:\Program Files\Java\jdk-17"
    ) else if exist "C:\Program Files\Java\jdk-21" (
        set "JAVA_HOME=C:\Program Files\Java\jdk-21"
    ) else if exist "C:\Program Files\Eclipse Adoptium\jdk-17" (
        set "JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-17"
    ) else if exist "C:\Program Files\Eclipse Adoptium\jdk-21" (
        set "JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-21"
    ) else (
        REM Try to find java.exe in PATH
        where java >nul 2>&1
        if !ERRORLEVEL! == 0 (
            for /f "delims=" %%i in ('where java') do (
                set "JAVA_PATH=%%i"
                REM Extract JAVA_HOME from java.exe path (go up from bin to jdk root)
                for %%j in ("%%~dpi..") do set "JAVA_HOME=%%~fj"
                goto :found_java
            )
        )
        echo Error: JAVA_HOME is not set and Java could not be found.
        echo Please set JAVA_HOME environment variable or install Java 17+.
        exit /b 1
        :found_java
    )
)

if not exist "%JAVA_HOME%\bin\java.exe" (
    echo Error: Java not found at %JAVA_HOME%
    exit /b 1
)

set MAVEN_PROJECTBASEDIR=%~dp0
cd /d "%MAVEN_PROJECTBASEDIR%"

set WRAPPER_JAR="%MAVEN_PROJECTBASEDIR%.mvn\wrapper\maven-wrapper.jar"
set WRAPPER_URL=https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar

if exist "%MAVEN_PROJECTBASEDIR%.mvn\wrapper\maven-wrapper.properties" (
    for /f "tokens=1,2 delims==" %%a in ('type "%MAVEN_PROJECTBASEDIR%.mvn\wrapper\maven-wrapper.properties"') do (
        if "%%a"=="wrapperUrl" set WRAPPER_URL=%%b
    )
)

if not exist "%MAVEN_PROJECTBASEDIR%.mvn\wrapper\maven-wrapper.jar" (
    echo Downloading Maven Wrapper...
    powershell -Command "Invoke-WebRequest -Uri '%WRAPPER_URL%' -OutFile '%MAVEN_PROJECTBASEDIR%.mvn\wrapper\maven-wrapper.jar' -UseBasicParsing"
    if !ERRORLEVEL! NEQ 0 (
        echo Failed to download Maven Wrapper
        exit /b 1
    )
)

"%JAVA_HOME%\bin\java.exe" -classpath "%MAVEN_PROJECTBASEDIR%.mvn\wrapper\maven-wrapper.jar" "-Dmaven.multiModuleProjectDirectory=%MAVEN_PROJECTBASEDIR%" org.apache.maven.wrapper.MavenWrapperMain %*
