#!/bin/bash

# Change to script directory
cd "$(dirname "$0")"

# Set variables
APP_NAME="part1-currency-converter-soap"
PORT="8080"

echo
echo "[1/3] Starting local deployment process..."

# Build Image
echo
echo "[2/3] Building Docker Image..."
if ! docker build -t "$APP_NAME:latest" .; then
    echo "Error: Docker Build failed"
    exit 1
fi

# Run Container
echo
echo "[3/3] Starting Container..."
echo "Stopping existing container if running..."
docker stop "$APP_NAME" 2>/dev/null
docker rm "$APP_NAME" 2>/dev/null

echo "Starting new container..."
if ! docker run -d --name "$APP_NAME" -p "$PORT:80" "$APP_NAME:latest"; then
    echo "Error: Container start failed"
    exit 1
fi

echo
echo "Deployment successful!"
echo "URL: http://localhost:$PORT"
echo
echo "Container logs:"
docker logs "$APP_NAME"

echo
read -p "Press Enter to continue..."