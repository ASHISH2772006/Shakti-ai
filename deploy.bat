@echo off
setlocal enabledelayedexpansion

echo ========================================
echo    SHAKTI AI - One-Click Deployment
echo ========================================
echo.
echo This script will:
echo 1. Build the release APK
echo 2. Sign the APK (create keystore if needed)
echo 3. Prepare for GitHub Release
echo.
echo Press any key to start...
pause >nul

echo.
echo ========================================
echo STEP 1: Building Release APK
echo ========================================
echo.

call gradlew.bat clean assembleRelease

if errorlevel 1 (
    echo.
    echo ERROR: Build failed!
    pause
    exit /b 1
)

echo.
echo ✓ Build successful!
echo.

echo ========================================
echo STEP 2: Signing APK
echo ========================================
echo.

:: Check if keystore exists
if exist "shakti-ai-key.jks" (
    echo ✓ Keystore found: shakti-ai-key.jks
    echo.
    goto SIGN_APK
) else (
    echo Creating new keystore...
    echo.
    echo IMPORTANT: Remember the password you enter!
    echo.
    
    keytool -genkey -v -keystore shakti-ai-key.jks -keyalg RSA -keysize 2048 -validity 10000 -alias shakti-ai
    
    if errorlevel 1 (
        echo.
        echo ERROR: Failed to create keystore
        pause
        exit /b 1
    )
    echo.
    echo ✓ Keystore created successfully!
    echo.
)

:SIGN_APK
echo Signing APK...
echo.

jarsigner -verbose -sigalg SHA256withRSA -digestalg SHA-256 -keystore shakti-ai-key.jks app\build\outputs\apk\release\app-release-unsigned.apk shakti-ai

if errorlevel 1 (
    echo.
    echo ERROR: Failed to sign APK
    pause
    exit /b 1
)

echo.
echo ✓ APK signed successfully!
echo.

:: Verify signature
echo Verifying signature...
jarsigner -verify app\build\outputs\apk\release\app-release-unsigned.apk >nul 2>&1

if errorlevel 1 (
    echo WARNING: Signature verification failed
) else (
    echo ✓ Signature verified!
)

echo.

:: Try to optimize with zipalign
where zipalign >nul 2>&1
if not errorlevel 1 (
    echo Optimizing APK...
    zipalign -v 4 app\build\outputs\apk\release\app-release-unsigned.apk shakti-ai-v1.0.0.apk
    if not errorlevel 1 (
        echo ✓ APK optimized!
    ) else (
        copy app\build\outputs\apk\release\app-release-unsigned.apk shakti-ai-v1.0.0.apk
    )
) else (
    echo Zipalign not found, copying APK...
    copy app\build\outputs\apk\release\app-release-unsigned.apk shakti-ai-v1.0.0.apk
)

echo.
echo ========================================
echo STEP 3: Getting File Size
echo ========================================
echo.

for %%A in (shakti-ai-v1.0.0.apk) do set SIZE=%%~zA
set /a SIZE_MB=!SIZE! / 1048576
echo APK Size: !SIZE_MB! MB
echo.

echo ========================================
echo ✓ SUCCESS! Your APK is Ready!
echo ========================================
echo.
echo Signed APK: shakti-ai-v1.0.0.apk
echo Size: !SIZE_MB! MB
echo.
echo ========================================
echo NEXT STEPS - Create GitHub Release:
echo ========================================
echo.
echo 1. Go to: https://github.com/ASHISH2772006/Shakti-ai/releases
echo 2. Click "Create a new release"
echo 3. Tag: v1.0.0
echo 4. Title: SHAKTI AI v1.0.0 - Emergency Safety App
echo 5. Upload file: shakti-ai-v1.0.0.apk
echo 6. Click "Publish release"
echo.
echo Your download link will be:
echo https://github.com/ASHISH2772006/Shakti-ai/releases/download/v1.0.0/shakti-ai-v1.0.0.apk
echo.
echo ========================================
echo IMPORTANT: Backup Your Keystore!
echo ========================================
echo.
echo Copy this file to a safe place:
echo shakti-ai-key.jks
echo.
echo You'll need it for ALL future updates!
echo.
echo Would you like to open GitHub Releases page now? (Y/N)
set /p OPEN_BROWSER=

if /i "%OPEN_BROWSER%"=="Y" (
    start https://github.com/ASHISH2772006/Shakti-ai/releases/new
    echo.
    echo ✓ Browser opened! Upload your APK there.
)

echo.
echo Press any key to exit...
pause >nul
