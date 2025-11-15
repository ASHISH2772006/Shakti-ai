@echo off
echo ========================================
echo SHAKTI AI - APK Signing Script
echo ========================================
echo.

:: Check if keystore exists
if exist "shakti-ai-key.jks" (
    echo Keystore found: shakti-ai-key.jks
    goto SIGN_APK
) else (
    echo Keystore not found. Creating new keystore...
    echo.
    echo Please provide the following information:
    echo.
    
    :: Generate keystore
    keytool -genkey -v -keystore shakti-ai-key.jks -keyalg RSA -keysize 2048 -validity 10000 -alias shakti-ai
    
    if errorlevel 1 (
        echo ERROR: Failed to create keystore
        pause
        exit /b 1
    )
    echo.
    echo Keystore created successfully!
    echo.
)

:SIGN_APK
echo.
echo Signing APK...
echo.

:: Check if unsigned APK exists
if not exist "app\build\outputs\apk\release\app-release-unsigned.apk" (
    echo ERROR: Unsigned APK not found!
    echo Please build the release APK first using: gradlew assembleRelease
    pause
    exit /b 1
)

:: Sign the APK
jarsigner -verbose -sigalg SHA256withRSA -digestalg SHA-256 -keystore shakti-ai-key.jks app\build\outputs\apk\release\app-release-unsigned.apk shakti-ai

if errorlevel 1 (
    echo ERROR: Failed to sign APK
    pause
    exit /b 1
)

echo.
echo APK signed successfully!
echo.

:: Verify the signature
echo Verifying signature...
jarsigner -verify -verbose -certs app\build\outputs\apk\release\app-release-unsigned.apk

echo.
echo Optimizing APK (zipalign)...
echo.

:: Check if zipalign is available
where zipalign >nul 2>nul
if errorlevel 1 (
    echo WARNING: zipalign not found in PATH
    echo Skipping zipalign optimization
    echo Your APK is signed but not optimized
    goto COPY_APK
)

:: Align the APK
zipalign -v 4 app\build\outputs\apk\release\app-release-unsigned.apk shakti-ai-release-signed.apk

if errorlevel 1 (
    echo WARNING: Failed to optimize APK
    goto COPY_APK
)

echo.
echo APK optimized successfully!
goto DONE

:COPY_APK
:: Copy the signed APK to root directory
copy app\build\outputs\apk\release\app-release-unsigned.apk shakti-ai-release-signed.apk

:DONE
echo.
echo ========================================
echo APK SIGNING COMPLETE!
echo ========================================
echo.
echo Signed APK location: shakti-ai-release-signed.apk
echo.
echo Next steps:
echo 1. Test the APK on your device
echo 2. Create a GitHub Release
echo 3. Upload the signed APK
echo.
echo IMPORTANT: Backup your keystore file (shakti-ai-key.jks)!
echo You'll need it for all future updates!
echo.
pause
