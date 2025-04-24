@echo off
cd /d "%~dp0"
setlocal EnableDelayedExpansion

:: Set variables
set ACR_NAME=carrental
set ACR_USERNAME=carrental
set ACR_PASSWORD=PcVqOtQSd7a9IcuOpJliUF0JbOEOzL98untd2VswXn+ACRCvg1tj
set APP_NAME=part2-carrental-currency-converter-grpc
set RESOURCE_GROUP=se-carrental
set APP_PLAN_NAME=app-service-plan

echo.
echo [1/8] Starting deployment process...

:: Check Azure Login Status
call az account show >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo Azure Login required!
    set /p LOGIN_CHOICE="Do you want to login to Azure? (y/n): "
    if /i "!LOGIN_CHOICE!"=="y" (
        echo.
        echo [2/8] Azure Login...
        call az login --use-device-code
        if !ERRORLEVEL! NEQ 0 (
            echo Error: Azure Login failed
            pause
            exit /b 1
        )
    ) else (
        echo Deployment cancelled - Login required
        pause
        exit /b 1
    )
) else (
    echo Azure Login already exists - skipping login
)

:: Check Resource Group
echo.
echo [3/8] Checking Resource Group...
call az group show --name %RESOURCE_GROUP% >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo Resource Group does not exist. Creating %RESOURCE_GROUP%...
    call az group create --name %RESOURCE_GROUP% --location germanywestcentral
)

:: ACR Login
echo.
echo [4/8] Azure Container Registry Login...
call az acr login --name %ACR_NAME%
if %ERRORLEVEL% NEQ 0 (
    echo Error: ACR Login failed
    pause
    exit /b 1
)

:: Build Image
echo.
echo [5/8] Building Docker Image...
call docker build -t %ACR_NAME%.azurecr.io/%APP_NAME%:latest .
if %ERRORLEVEL% NEQ 0 (
    echo Error: Docker Build failed
    pause
    exit /b 1
)

:: Push Image
echo.
echo [6/8] Pushing Image to Azure...
call docker push %ACR_NAME%.azurecr.io/%APP_NAME%:latest
if %ERRORLEVEL% NEQ 0 (
    echo Error: Docker Push failed
    pause
    exit /b 1
)

:: Create App Service Plan
echo.
echo [7/8] Creating App Service Plan...
call az appservice plan create ^
    --resource-group %RESOURCE_GROUP% ^
    --name %APP_PLAN_NAME% ^
    --sku B1 ^
    --is-linux

:: Create and configure Web App
echo.
echo [8/8] Creating and configuring Web App...
call az webapp create ^
    --resource-group %RESOURCE_GROUP% ^
    --plan %APP_PLAN_NAME% ^
    --name %APP_NAME% ^
    --deployment-container-image-name %ACR_NAME%.azurecr.io/%APP_NAME%:latest

:: Configure Container Registry
call az webapp config container set ^
    --resource-group %RESOURCE_GROUP% ^
    --name %APP_NAME% ^
    --container-registry-url https://%ACR_NAME%.azurecr.io ^
    --container-registry-user %ACR_USERNAME% ^
    --container-registry-password %ACR_PASSWORD% ^
    --container-image-name %ACR_NAME%.azurecr.io/%APP_NAME%:latest ^
    --enable-cd true

:: Set environment variables
call az webapp config appsettings set ^
    --resource-group %RESOURCE_GROUP% ^
    --name %APP_NAME% ^
    --settings WEBSITES_PORT=50051

if %ERRORLEVEL% NEQ 0 (
    echo Error: Web App creation failed
    pause
    exit /b 1
)

echo.
echo Starting Web App...
timeout /t 5 /nobreak > nul

echo.
echo Deployment successful!
echo URL: https://%APP_NAME%.azurewebsites.net

echo.
pause