@echo off
cd /d "%~dp0"

:: Set variables
set APP_NAME=part2-currency-converter-grpc
set PORT=50051

echo.
echo [1/3] Starting local deployment process...

:: Build Image
echo.
echo [2/3] Building Docker Image...
docker build -t %APP_NAME%:latest .
if %ERRORLEVEL% NEQ 0 (
    echo Error: Docker Build failed
    pause
    exit /b 1
)

:: Run Container
echo.
echo [3/3] Starting Container...
echo Stopping existing container if running...
docker stop %APP_NAME% 2>nul
docker rm %APP_NAME% 2>nul

echo Starting new container...
docker run -d --name %APP_NAME% -p %PORT%:%PORT% %APP_NAME%:latest

if %ERRORLEVEL% NEQ 0 (
    echo Error: Container start failed
    pause
    exit /b 1
)

echo.
echo Deployment successful!
echo URL: http://localhost:%PORT%
echo.
echo Container logs:
docker logs %APP_NAME%

echo.
pause