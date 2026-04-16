@echo off

:: 1. Set PATH_TO_FX using a relative path.
::    The "%~dp0" variable refers to the directory where the batch file is located.
::    This makes the path relative to the batch file, ensuring portability.
set PATH_TO_FX=%~dp0javafx-sdk-25.0.1\lib

:: Check if the path is valid before proceeding
if not exist "%PATH_TO_FX%" (
    echo Error: JavaFX SDK library path not found.
    echo Expected path: %PATH_TO_FX%
    echo Please ensure the 'javafx-sdk-25.0.1' folder is in the same directory as this batch file.
    pause
    exit /b 1
)

echo JavaFX SDK Path Set: %PATH_TO_TO%

---

:: 2. Find all Java source files and list them in sources.txt
echo Finding source files...
dir /s /b src\*.java > sources.txt

:: 3. Compile the Java files
echo Compiling project...
javac @sources.txt -d bin --module-path "%PATH_TO_FX%" --add-modules javafx.controls,javafx.fxml

:: Check for compilation success before running
if %errorlevel% neq 0 (
    echo.
    echo **Compilation FAILED! Please check the code for errors.**
    pause
    exit /b 1
)

:: Clean up the sources.txt file
del sources.txt

---

:: 4. Run the main class
echo Running the game...
java -cp bin;resources --module-path "%PATH_TO_FX%" --add-modules javafx.controls,javafx.fxml Control.Main

pause