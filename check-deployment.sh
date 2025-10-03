#!/bin/bash

# Pre-deployment Checklist Script
# Run this before deploying to ensure everything is ready

echo "🔍 Startup Showcase - Pre-Deployment Checklist"
echo "=============================================="
echo ""

# Colors
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

PASS=0
FAIL=0
WARN=0

check_pass() {
    echo -e "${GREEN}✓${NC} $1"
    ((PASS++))
}

check_fail() {
    echo -e "${RED}✗${NC} $1"
    ((FAIL++))
}

check_warn() {
    echo -e "${YELLOW}⚠${NC} $1"
    ((WARN++))
}

check_info() {
    echo -e "${BLUE}ℹ${NC} $1"
}

echo "Checking prerequisites..."
echo ""

# Check 1: Gradle wrapper
if [ -f "./gradlew" ]; then
    check_pass "Gradle wrapper found"
else
    check_fail "Gradle wrapper not found"
fi

# Check 2: google-services.json
if [ -f "app/google-services.json" ]; then
    # Check if it's the template or real config
    if grep -q "YOUR_PROJECT_NUMBER" app/google-services.json; then
        check_warn "google-services.json is template - replace with real Firebase config"
    else
        check_pass "google-services.json configured"
    fi
else
    check_fail "google-services.json not found in app/ directory"
fi

# Check 3: Keystore (for release builds)
if [ -f "startup-showcase-release.keystore" ] || [ -f "keystore.properties" ]; then
    check_pass "Keystore configured for release builds"
else
    check_warn "No keystore found - needed for release builds (run ./create-keystore.sh)"
fi

# Check 4: Build configuration
if [ -f "app/build.gradle.kts" ]; then
    check_pass "Build configuration found"
    
    # Check version
    VERSION_NAME=$(grep "versionName" app/build.gradle.kts | head -1 | sed 's/.*"\(.*\)".*/\1/')
    VERSION_CODE=$(grep "versionCode" app/build.gradle.kts | head -1 | sed 's/[^0-9]*//g')
    
    if [ -n "$VERSION_NAME" ] && [ -n "$VERSION_CODE" ]; then
        check_info "Version: $VERSION_NAME (code: $VERSION_CODE)"
    fi
fi

# Check 5: AndroidManifest
if [ -f "app/src/main/AndroidManifest.xml" ]; then
    check_pass "AndroidManifest.xml found"
else
    check_fail "AndroidManifest.xml not found"
fi

# Check 6: ProGuard rules
if [ -f "app/proguard-rules.pro" ]; then
    check_pass "ProGuard rules configured"
else
    check_warn "ProGuard rules not found"
fi

# Check 7: .gitignore
if [ -f ".gitignore" ]; then
    if grep -q "keystore" .gitignore && grep -q "google-services.json" .gitignore; then
        check_pass ".gitignore properly configured"
    else
        check_warn ".gitignore missing sensitive files"
    fi
else
    check_warn ".gitignore not found"
fi

# Check 8: String resources
if [ -f "app/src/main/res/values/strings.xml" ]; then
    APP_NAME=$(grep "app_name" app/src/main/res/values/strings.xml | sed 's/.*>\(.*\)<.*/\1/')
    if [ -n "$APP_NAME" ]; then
        check_info "App name: $APP_NAME"
    fi
fi

# Check 9: Icons
if [ -d "app/src/main/res/mipmap-xxxhdpi" ]; then
    check_pass "App icons present"
else
    check_warn "High-res app icons not found"
fi

# Check 10: Network permissions
if grep -q "android.permission.INTERNET" app/src/main/AndroidManifest.xml; then
    check_pass "Internet permission declared"
else
    check_fail "Internet permission missing"
fi

echo ""
echo "═══════════════════════════════════════"
echo "Summary:"
echo -e "${GREEN}✓ Passed: $PASS${NC}"
echo -e "${YELLOW}⚠ Warnings: $WARN${NC}"
echo -e "${RED}✗ Failed: $FAIL${NC}"
echo "═══════════════════════════════════════"
echo ""

if [ $FAIL -gt 0 ]; then
    echo -e "${RED}❌ Not ready for deployment!${NC}"
    echo "Please fix the failed checks above."
    exit 1
elif [ $WARN -gt 0 ]; then
    echo -e "${YELLOW}⚠ Deployment possible but warnings present${NC}"
    echo "Review warnings before proceeding."
    echo ""
    read -p "Continue anyway? (y/n): " continue
    if [ "$continue" != "y" ]; then
        exit 1
    fi
else
    echo -e "${GREEN}✅ Ready for deployment!${NC}"
fi

echo ""
echo "Next steps:"
echo "1. Test the app thoroughly"
echo "2. Run: ./build.sh to build release version"
echo "3. Follow DEPLOYMENT_GUIDE.md for distribution"
echo ""
echo "For production deployment checklist, see:"
echo "DEPLOYMENT_GUIDE.md Section 7"
