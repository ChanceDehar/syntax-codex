@echo off
echo ========================================
echo Syntax Codex - Final Build
echo ========================================
echo.

echo [1/2] Building with Maven...
call mvn package
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Build failed!
    pause
    exit /b 1
)
echo.

echo [2/2] Creating portable app...

REM Delete existing desktop shortcut if it exists
if exist "%USERPROFILE%\Desktop\Syntax Codex.lnk" (
    echo Deleting existing desktop shortcut...
    del "%USERPROFILE%\Desktop\Syntax Codex.lnk"
)

REM Delete existing portable directory if it exists
if exist "SyntaxCodex-Portable" (
    echo Deleting existing portable directory...
    rd /s /q SyntaxCodex-Portable
)

REM Create portable directory
mkdir SyntaxCodex-Portable\app

REM Copy JAR and all dependencies
copy target\syntax-codex-1.0.0.jar SyntaxCodex-Portable\app\ > nul
xcopy /E /I /Y target\libs SyntaxCodex-Portable\app\libs > nul

REM Copy resources
xcopy /E /I /Y src\main\resources SyntaxCodex-Portable\app\resources > nul

REM Copy icon
if exist "src\main\resources\icon.ico" (
    copy src\main\resources\icon.ico SyntaxCodex-Portable\icon.ico > nul
) else (
    echo WARNING: icon.ico not found, shortcut will use default icon
)

REM Create launcher - Debug
(
echo @echo off
echo cd /d "%%~dp0app"
echo java --module-path libs --add-modules javafx.controls,javafx.fxml -cp "syntax-codex-1.0.0.jar;libs/*" com.syntaxcodex.App
echo if ERRORLEVEL 1 pause
) > SyntaxCodex-Portable\SyntaxCodex-Debug.bat

REM Create launcher - No console
(
echo @echo off
echo cd /d "%%~dp0app"
echo start javaw --module-path libs --add-modules javafx.controls,javafx.fxml -cp "syntax-codex-1.0.0.jar;libs/*" com.syntaxcodex.App
) > SyntaxCodex-Portable\SyntaxCodex.bat

REM Create Desktop Shortcut with custom icon
echo Creating desktop shortcut...

powershell -NoProfile -Command ^
"$s=(New-Object -COM WScript.Shell).CreateShortcut([Environment]::GetFolderPath('Desktop')+'\Syntax Codex.lnk'); ^
$s.TargetPath='%CD%\SyntaxCodex-Portable\SyntaxCodex.bat'; ^
$s.WorkingDirectory='%CD%\SyntaxCodex-Portable'; ^
$s.IconLocation='%CD%\SyntaxCodex-Portable\icon.ico'; ^
$s.Save()"

REM Create README
(
echo ========================================
echo Syntax Codex - Portable Version
echo ========================================
echo.
echo REQUIREMENTS: Java 17+
echo.
echo TO RUN:
echo - Use Desktop shortcut (recommended)
echo - Or double-click SyntaxCodex.bat
echo.
echo FOR DEBUGGING:
echo - Run SyntaxCodex-Debug.bat
echo.
echo TO DISTRIBUTE:
echo - Zip the entire SyntaxCodex-Portable folder
) > SyntaxCodex-Portable\README.txt

echo.
echo ========================================
echo BUILD COMPLETE!
echo ========================================
echo.
echo Your app is ready in: SyntaxCodex-Portable\
echo Desktop shortcut created: Syntax Codex
echo.
echo Run SyntaxCodex.bat or use the desktop shortcut to launch the app.
echo.

pause