# runGame.ps1

# ===== SETTINGS =====
$SourceDir = "src"
$OutputDir = "out"
$JavaFXDir = "javafx-sdk-25.0.1"
$JavaFXLib = "$JavaFXDir\lib"
$MainClass = "Control.Main"

# Check if the output directory exists, if not, create it
if (-not (Test-Path $OutputDir)) {
    Write-Host "Creating output directory: $OutputDir"
    New-Item -ItemType Directory -Force -Path $OutputDir | Out-Null
}

# --- 1. COMPILATION (Javac) ---

Write-Host "`n============================================" -ForegroundColor Green
Write-Host "Compiling Strip Mining Game (PowerShell)..." -ForegroundColor Green
Write-Host "============================================" -ForegroundColor Green

# Find all Java source files recursively, which PowerShell handles easily.
$JavaFiles = Get-ChildItem -Path $SourceDir -Filter "*.java" -Recurse | Select-Object -ExpandProperty FullName

# Construct the Classpath. PowerShell uses semicolon (;) for classpath delimiters just like batch.
$ClassPathCommon = "$OutputDir;$JavaFXLib\*"

# Execute compilation
& javac `
    -d $OutputDir `
    -cp $ClassPathCommon `
    --module-path "$JavaFXLib" `
    --add-modules javafx.controls,javafx.fxml `
    $JavaFiles

if ($LASTEXITCODE -ne 0) {
    Write-Error "Compilation failed! Check your syntax or missing imports."
    Read-Host "Press Enter to exit..."
    exit $LASTEXITCODE
}

# --- 1.5. COPY RESOURCES ---

Write-Host "`n============================================" -ForegroundColor Green
Write-Host "Copying Resources..." -ForegroundColor Green
Write-Host "============================================" -ForegroundColor Green

# Copy all contents of the 'resources' folder to the 'out' directory
Copy-Item -Path "resources\*" -Destination "$OutputDir\" -Recurse -Force

# --- 2. EXECUTION (Java) ---

Write-Host "`n============================================" -ForegroundColor Green
Write-Host "Running Game..." -ForegroundColor Green
Write-Host "============================================" -ForegroundColor Green

# Execute the main Java class
& java `
    -cp $ClassPathCommon `
    --module-path "$JavaFXLib" `
    --add-modules javafx.controls,javafx.fxml `
    $MainClass

Write-Host "`n============================================" -ForegroundColor Green
Write-Host "Game execution finished." -ForegroundColor Green
Write-Host "============================================" -ForegroundColor Green
Read-Host "Press Enter to exit..."