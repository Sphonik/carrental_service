@echo off
cd /d "%~dp0"
setlocal EnableDelayedExpansion

:: Set variables
set ACR_NAME=carrental
set ACR_USERNAME=carrental
set ACR_PASSWORD=PcVqOtQSd7a9IcuOpJliUF0JbOEOzL98untd2VswXn+ACRCvg1tj
set CONTAINER_NAME=part2-car-booking-service
set RESOURCE_GROUP=se-carrental
set DNS_NAME_LABEL=part2-car-booking-service

echo.
echo [1/7] Starting deployment process...

:: Check Azure Login Status
call az account show >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo Azure Login required!
    set /p LOGIN_CHOICE="Do you want to login to Azure? (y/n): "
    if /i "!LOGIN_CHOICE!"=="y" (
        echo.
        echo [2/7] Azure Login...
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
echo [3/7] Checking Resource Group...
call az group show --name %RESOURCE_GROUP% >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo Resource Group does not exist. Creating %RESOURCE_GROUP%...
    call az group create --name %RESOURCE_GROUP% --location germanywestcentral
)

:: ACR Login
echo.
echo [4/7] Azure Container Registry Login...
call az acr login --name %ACR_NAME%
if %ERRORLEVEL% NEQ 0 (
    echo Error: ACR Login failed
    pause
    exit /b 1
)

:: Build Image
echo.
echo [5/7] Building Docker Image...
call docker build -t %ACR_NAME%.azurecr.io/%CONTAINER_NAME%:latest .
if %ERRORLEVEL% NEQ 0 (
    echo Error: Docker Build failed
    pause
    exit /b 1
)

:: Push Image
echo.
echo [6/7] Pushing Image to Azure...
call docker push %ACR_NAME%.azurecr.io/%CONTAINER_NAME%:latest
if %ERRORLEVEL% NEQ 0 (
    echo Error: Docker Push failed
    pause
    exit /b 1
)

:: Create Container Instance
echo.
echo [7/7] Creating Container Instance...
call az container create ^
    --resource-group %RESOURCE_GROUP% ^
    --name %CONTAINER_NAME% ^
    --image %ACR_NAME%.azurecr.io/%CONTAINER_NAME%:latest ^
    --registry-login-server %ACR_NAME%.azurecr.io ^
    --registry-username %ACR_USERNAME% ^
    --registry-password %ACR_PASSWORD% ^
    --dns-name-label %DNS_NAME_LABEL% ^
    --ports 8100 ^
    --environment-variables WEBSITES_PORT=8100 ^
    --os-type Linux ^
    --cpu 1 ^
    --memory 1.5

if %ERRORLEVEL% NEQ 0 (
    echo Error: Container Instance creation failed
    pause
    exit /b 1
)

echo.
echo Waiting for Container Instance to start...
timeout /t 10 /nobreak > nul

echo.
echo Deployment successful!
echo URL: http://%DNS_NAME_LABEL%.germanywestcentral.azurecontainer.io:8100

echo.
pause