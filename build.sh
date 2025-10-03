#!/bin/bash

# Startup Showcase - Quick Build Script
# This script helps you build the app quickly for different environments

set -e  # Exit on error

echo "🚀 Startup Showcase - Build Script"
echo "=================================="
echo ""

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Function to print colored output
print_success() {
    echo -e "${GREEN}✓ $1${NC}"
}

print_error() {
    echo -e "${RED}✗ $1${NC}"
}

print_info() {
    echo -e "${YELLOW}ℹ $1${NC}"
}

# Check if gradlew exists
if [ ! -f "./gradlew" ]; then
    print_error "gradlew not found. Are you in the project root directory?"
    exit 1
fi

# Make gradlew executable
chmod +x ./gradlew

# Menu
echo "Select build type:"
echo "1) Debug Build (for development/testing)"
echo "2) Release APK (signed, for distribution)"
echo "3) Release AAB (for Google Play Store)"
echo "4) Clean build files"
echo "5) Run tests"
echo "6) Install debug on connected device"
echo ""
read -p "Enter your choice (1-6): " choice

case $choice in
    1)
        print_info "Building debug APK..."
        ./gradlew assembleDebug
        if [ $? -eq 0 ]; then
            print_success "Debug APK built successfully!"
            echo "Location: app/build/outputs/apk/debug/app-debug.apk"
        else
            print_error "Build failed!"
            exit 1
        fi
        ;;
    2)
        print_info "Building release APK..."
        if [ ! -f "keystore.properties" ]; then
            print_error "keystore.properties not found!"
            echo "Please create keystore.properties with your signing configuration."
            echo "See DEPLOYMENT_GUIDE.md for instructions."
            exit 1
        fi
        ./gradlew assembleRelease
        if [ $? -eq 0 ]; then
            print_success "Release APK built successfully!"
            echo "Location: app/build/outputs/apk/release/app-release.apk"
            
            # Get APK size
            APK_SIZE=$(du -h app/build/outputs/apk/release/app-release.apk | cut -f1)
            echo "APK Size: $APK_SIZE"
        else
            print_error "Build failed!"
            exit 1
        fi
        ;;
    3)
        print_info "Building release AAB (Android App Bundle)..."
        if [ ! -f "keystore.properties" ]; then
            print_error "keystore.properties not found!"
            echo "Please create keystore.properties with your signing configuration."
            echo "See DEPLOYMENT_GUIDE.md for instructions."
            exit 1
        fi
        ./gradlew bundleRelease
        if [ $? -eq 0 ]; then
            print_success "Release AAB built successfully!"
            echo "Location: app/build/outputs/bundle/release/app-release.aab"
            
            # Get AAB size
            AAB_SIZE=$(du -h app/build/outputs/bundle/release/app-release.aab | cut -f1)
            echo "AAB Size: $AAB_SIZE"
        else
            print_error "Build failed!"
            exit 1
        fi
        ;;
    4)
        print_info "Cleaning build files..."
        ./gradlew clean
        if [ $? -eq 0 ]; then
            print_success "Clean completed!"
        else
            print_error "Clean failed!"
            exit 1
        fi
        ;;
    5)
        print_info "Running tests..."
        ./gradlew test
        if [ $? -eq 0 ]; then
            print_success "All tests passed!"
        else
            print_error "Tests failed!"
            exit 1
        fi
        ;;
    6)
        print_info "Installing debug APK on connected device..."
        ./gradlew installDebug
        if [ $? -eq 0 ]; then
            print_success "App installed successfully!"
            print_info "You can now launch the app on your device."
        else
            print_error "Installation failed!"
            echo "Make sure a device is connected and USB debugging is enabled."
            exit 1
        fi
        ;;
    *)
        print_error "Invalid choice!"
        exit 1
        ;;
esac

echo ""
print_success "Done!"
