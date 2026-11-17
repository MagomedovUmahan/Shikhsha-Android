@echo off
cd /d "%~dp0"
where node >nul 2>&1
if errorlevel 1 (
  echo Install Node.js 24 LTS, reopen this file and try again.
  pause
  exit /b 1
)
if not exist node_modules (
  call npm ci
  if errorlevel 1 (
    pause
    exit /b 1
  )
)
echo Open Expo Go on your iPhone. Use the same Wi-Fi and scan the QR code.
call npm run iphone
pause
