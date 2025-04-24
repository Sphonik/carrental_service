#!/bin/bash

# Set variables
ACR_NAME="carrental"
ACR_USERNAME="carrental"
ACR_PASSWORD="PcVqOtQSd7a9IcuOpJliUF0JbOEOzL98untd2VswXn+ACRCvg1tj"
APP_NAME="part1-carrental-currency-converter-soap"
RESOURCE_GROUP="se-carrental"
APP_PLAN_NAME="app-service-plan"

# Change to script directory
cd "$(dirname "$0")"

echo
echo "[1/8] Starting deployment process..."

# Check Azure Login Status
if ! az account show >/dev/null 2>&1; then
    echo "Azure Login required!"
    read -p "Do you want to login to Azure? (y/n): " LOGIN_CHOICE
    if [[ $LOGIN_CHOICE =~ ^[Yy]$ ]]; then
        echo
        echo "[2/8] Azure Login..."
        if ! az login --use-device-code; then
            echo "Error: Azure Login failed"
            exit 1
        fi
    else
        echo "Deployment cancelled - Login required"
        exit 1
    fi
else
    echo "Azure Login already exists - skipping login"
fi

# Check Resource Group
echo
echo "[3/8] Checking Resource Group..."
if ! az group show --name "$RESOURCE_GROUP" >/dev/null 2>&1; then
    echo "Resource Group does not exist. Creating $RESOURCE_GROUP..."
    az group create --name "$RESOURCE_GROUP" --location germanywestcentral
fi

# ACR Login
echo
echo "[4/8] Azure Container Registry Login..."
if ! az acr login --name "$ACR_NAME"; then
    echo "Error: ACR Login failed"
    exit 1
fi

# Build Image
echo
echo "[5/8] Building Docker Image..."
if ! docker build -t "$ACR_NAME.azurecr.io/$APP_NAME:latest" .; then
    echo "Error: Docker Build failed"
    exit 1
fi

# Push Image
echo
echo "[6/8] Pushing Image to Azure..."
if ! docker push "$ACR_NAME.azurecr.io/$APP_NAME:latest"; then
    echo "Error: Docker Push failed"
    exit 1
fi

# Create App Service Plan
echo
echo "[7/8] Creating App Service Plan..."
az appservice plan create \
    --resource-group "$RESOURCE_GROUP" \
    --name "$APP_PLAN_NAME" \
    --sku B1 \
    --is-linux

# Create and configure Web App
echo
echo "[8/8] Creating and configuring Web App..."
az webapp create \
    --resource-group "$RESOURCE_GROUP" \
    --plan "$APP_PLAN_NAME" \
    --name "$APP_NAME" \
    --deployment-container-image-name "$ACR_NAME.azurecr.io/$APP_NAME:latest"

# Configure Container Registry
az webapp config container set \
    --resource-group "$RESOURCE_GROUP" \
    --name "$APP_NAME" \
    --container-registry-url "https://$ACR_NAME.azurecr.io" \
    --container-registry-user "$ACR_USERNAME" \
    --container-registry-password "$ACR_PASSWORD" \
    --container-image-name "$ACR_NAME.azurecr.io/$APP_NAME:latest" \
    --enable-cd true

# Set environment variables
az webapp config appsettings set \
    --resource-group "$RESOURCE_GROUP" \
    --name "$APP_NAME" \
    --settings WEBSITES_PORT=50051

if [ $? -ne 0 ]; then
    echo "Error: Web App creation failed"
    exit 1
fi

echo
echo "Starting Web App..."
sleep 5

echo
echo "Deployment successful!"
echo "URL: https://$APP_NAME.azurewebsites.net"
echo
