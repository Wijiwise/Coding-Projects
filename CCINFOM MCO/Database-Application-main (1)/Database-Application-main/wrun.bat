@echo off
REM Create the output directory if it doesn't exist
if not exist "src\classes" mkdir "src\classes"

REM Path to connector
set MYSQL_JAR=lib\mysql-connector-j-9.4.0.jar
set OPENPDF_CORE_JAR=lib\openpdf-3.0.1-SNAPSHOT.jar
set OPENPDF_SWING_JAR=lib\pdf-swing-3.0.1-SNAPSHOT.jar

REM Build classpath (Windows uses semicolons, not colons)
set CP=%MYSQL_JAR%;%OPENPDF_CORE_JAR%;%OPENPDF_SWING_JAR%;src\classes

REM Compiling
javac -Xmaxerrs 10000 -Xmaxwarns 500 -Xlint ^
    -cp "%CP%" ^
    -d src\classes ^
    src\*.java src\model\*.java src\view\*.java src\controller\*.java src\util\*.java

REM Run main if compiling is successful
if %errorlevel%==0 (
    java -cp "%CP%" Main
)
