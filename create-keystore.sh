#!/bin/bash

# Startup Showcase - Create Keystore Script
# This script helps you create a keystore for signing your app

set -e

echo "🔐 Startup Showcase - Keystore Creation"
echo "======================================="
echo ""
echo "This script will help you create a keystore for signing your Android app."
echo "You'll need this for creating release builds."
echo ""

# Colors
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m'

# Check if keystore already exists
if [ -f "startup-showcase-release.keystore" ]; then
    echo -e "${RED}⚠ Warning: startup-showcase-release.keystore already exists!${NC}"
    read -p "Do you want to create a new one? This will backup the old one. (y/n): " confirm
    if [ "$confirm" != "y" ]; then
        echo "Cancelled."
        exit 0
    fi
    # Backup existing keystore
    mv startup-showcase-release.keystore "startup-showcase-release.keystore.backup.$(date +%Y%m%d_%H%M%S)"
    echo -e "${YELLOW}Old keystore backed up.${NC}"
fi

# Keystore configuration
KEYSTORE_FILE="startup-showcase-release.keystore"
KEY_ALIAS="startup-showcase"

echo ""
echo "Please provide the following information:"
echo ""

# Get keystore password
while true; do
    read -s -p "Enter keystore password (min 6 characters): " STORE_PASSWORD
    echo ""
    read -s -p "Confirm keystore password: " STORE_PASSWORD_CONFIRM
    echo ""
    
    if [ "$STORE_PASSWORD" != "$STORE_PASSWORD_CONFIRM" ]; then
        echo -e "${RED}Passwords don't match. Try again.${NC}"
    elif [ ${#STORE_PASSWORD} -lt 6 ]; then
        echo -e "${RED}Password must be at least 6 characters. Try again.${NC}"
    else
        break
    fi
done

# Get key password
echo ""
read -p "Use same password for key? (recommended) (y/n): " USE_SAME
if [ "$USE_SAME" = "y" ]; then
    KEY_PASSWORD="$STORE_PASSWORD"
else
    while true; do
        read -s -p "Enter key password (min 6 characters): " KEY_PASSWORD
        echo ""
        read -s -p "Confirm key password: " KEY_PASSWORD_CONFIRM
        echo ""
        
        if [ "$KEY_PASSWORD" != "$KEY_PASSWORD_CONFIRM" ]; then
            echo -e "${RED}Passwords don't match. Try again.${NC}"
        elif [ ${#KEY_PASSWORD} -lt 6 ]; then
            echo -e "${RED}Password must be at least 6 characters. Try again.${NC}"
        else
            break
        fi
    done
fi

# Get certificate information
echo ""
echo "Certificate Information:"
read -p "Your name (First and Last): " CERT_NAME
read -p "Organizational Unit (e.g., Development): " CERT_OU
read -p "Organization name (e.g., Your Company): " CERT_O
read -p "City or Locality: " CERT_L
read -p "State or Province: " CERT_ST
read -p "Country Code (2 letters, e.g., US): " CERT_C

echo ""
echo -e "${YELLOW}Creating keystore...${NC}"

# Create keystore
keytool -genkey -v \
    -keystore "$KEYSTORE_FILE" \
    -alias "$KEY_ALIAS" \
    -keyalg RSA \
    -keysize 2048 \
    -validity 10000 \
    -storepass "$STORE_PASSWORD" \
    -keypass "$KEY_PASSWORD" \
    -dname "CN=$CERT_NAME, OU=$CERT_OU, O=$CERT_O, L=$CERT_L, ST=$CERT_ST, C=$CERT_C"

if [ $? -eq 0 ]; then
    echo -e "${GREEN}✓ Keystore created successfully!${NC}"
    echo ""
    
    # Create keystore.properties
    echo "Creating keystore.properties..."
    cat > keystore.properties << EOF
storePassword=$STORE_PASSWORD
keyPassword=$KEY_PASSWORD
keyAlias=$KEY_ALIAS
storeFile=$KEYSTORE_FILE
EOF
    
    echo -e "${GREEN}✓ keystore.properties created!${NC}"
    echo ""
    
    # Security reminders
    echo -e "${YELLOW}🔒 IMPORTANT SECURITY REMINDERS:${NC}"
    echo "1. Keep your keystore file safe - you'll need it for ALL future updates!"
    echo "2. Never commit keystore.properties or .keystore files to version control"
    echo "3. Store your passwords securely (password manager recommended)"
    echo "4. Make a backup of your keystore in a secure location"
    echo ""
    
    # Verify it's in .gitignore
    if grep -q "keystore.properties" .gitignore && grep -q "*.keystore" .gitignore; then
        echo -e "${GREEN}✓ Keystore files are in .gitignore${NC}"
    else
        echo -e "${YELLOW}⚠ Adding keystore files to .gitignore...${NC}"
        echo "" >> .gitignore
        echo "# Keystore files - DO NOT COMMIT" >> .gitignore
        echo "keystore.properties" >> .gitignore
        echo "*.keystore" >> .gitignore
    fi
    
    echo ""
    echo -e "${GREEN}Setup complete! You can now build release versions.${NC}"
    echo "Run: ./build.sh and choose option 2 (Release APK) or 3 (Release AAB)"
    
else
    echo -e "${RED}✗ Failed to create keystore${NC}"
    exit 1
fi
