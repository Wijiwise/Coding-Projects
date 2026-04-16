@echo off

:: 1. Set PATH_TO_FX using a relative path for portability
set PATH_TO_FX=%~dp0javafx-sdk-25.0.1\lib

:: *** CHANGE THIS LINE ***
:: Set the output directory to src/docs3
set OUTPUT_DIR=src\docs3

:: Check for JavaFX path
if not exist "%PATH_TO_FX%" (
    echo Error: JavaFX SDK library path not found.
    echo Expected path: %PATH_TO_FX%
    pause
    exit /b 1
)

:: 2. Generate Javadoc
echo Generating Javadoc documentation into %OUTPUT_DIR%...

javadoc ^
    -d "%OUTPUT_DIR%" ^
    -sourcepath src ^
    -subpackages Control:Model:View ^
    --module-path "%PATH_TO_FX%" ^
    --add-modules javafx.controls,javafx.fxml ^
    -html5 ^
    -link "https://docs.oracle.com/en/java/javase/21/docs/api"

:: Check for success
if %errorlevel% neq 0 (
    echo.
    echo **Javadoc generation FAILED! Please check the console output.**
    pause
    exit /b 1
)

:: 3. Open the documentation
echo Javadoc successfully generated.
echo Opening index.html...
start "%OUTPUT_DIR%\index.html"

pause