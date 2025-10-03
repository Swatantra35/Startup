# Deployment Guide - Startup Showcase Android App

This guide covers multiple deployment options for your Android application, from development builds to production deployment on Google Play Store.

## Table of Contents
1. [Local Development Build](#1-local-development-build)
2. [Generate Signed APK/AAB](#2-generate-signed-apkaab)
3. [Google Play Store Deployment](#3-google-play-store-deployment)
4. [Firebase App Distribution (Beta Testing)](#4-firebase-app-distribution-beta-testing)
5. [Alternative Distribution Methods](#5-alternative-distribution-methods)
6. [CI/CD Pipeline Setup](#6-cicd-pipeline-setup)
7. [Post-Deployment Checklist](#7-post-deployment-checklist)

---

## 1. Local Development Build

### Debug Build (For Testing)

**Option A: Using Android Studio**
1. Connect your Android device via USB or start an emulator
2. Enable "Developer Options" and "USB Debugging" on your device
3. Click the green "Run" button (or press `Shift + F10`)
4. Select your device from the list
5. App will install and launch automatically

**Option B: Using Command Line**
```bash
# Navigate to project directory
cd /workspaces/Startup

# Build and install debug APK
./gradlew installDebug

# Or build APK without installing
./gradlew assembleDebug

# APK location: app/build/outputs/apk/debug/app-debug.apk
```

**Install APK manually:**
```bash
adb install app/build/outputs/apk/debug/app-debug.apk
```

---

## 2. Generate Signed APK/AAB

### Step 1: Create a Keystore

A keystore is required to sign your app for release.

```bash
keytool -genkey -v -keystore startup-showcase-release.keystore \
  -alias startup-showcase \
  -keyalg RSA \
  -keysize 2048 \
  -validity 10000
```

**Follow the prompts:**
- Enter keystore password (remember this!)
- Re-enter password
- Enter your name
- Enter organizational unit
- Enter organization name
- Enter city/locality
- Enter state/province
- Enter country code (e.g., US)
- Confirm information
- Enter key password (can be same as keystore password)

**Important:** Keep this keystore file safe! You'll need it for all future updates.

### Step 2: Configure Signing in Gradle

Create a `keystore.properties` file in your project root:

```bash
# Create keystore.properties
touch keystore.properties
```

Add the following content to `keystore.properties`:
```properties
storePassword=YOUR_KEYSTORE_PASSWORD
keyPassword=YOUR_KEY_PASSWORD
keyAlias=startup-showcase
storeFile=../startup-showcase-release.keystore
```

**Add to .gitignore** (prevent committing sensitive info):
```bash
echo "keystore.properties" >> .gitignore
echo "*.keystore" >> .gitignore
```

### Step 3: Update app/build.gradle.kts

Add this before the `android` block:

```kotlin
// Load keystore properties
val keystorePropertiesFile = rootProject.file("keystore.properties")
val keystoreProperties = java.util.Properties()
if (keystorePropertiesFile.exists()) {
    keystoreProperties.load(java.io.FileInputStream(keystorePropertiesFile))
}

android {
    // ... existing configuration ...
    
    signingConfigs {
        create("release") {
            storeFile = file(keystoreProperties["storeFile"] as String)
            storePassword = keystoreProperties["storePassword"] as String
            keyAlias = keystoreProperties["keyAlias"] as String
            keyPassword = keystoreProperties["keyPassword"] as String
        }
    }
    
    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
    }
}
```

### Step 4: Build Release APK

```bash
# Clean previous builds
./gradlew clean

# Build release APK
./gradlew assembleRelease

# Location: app/build/outputs/apk/release/app-release.apk
```

### Step 5: Build Android App Bundle (AAB) - Recommended for Play Store

```bash
# Build release AAB
./gradlew bundleRelease

# Location: app/build/outputs/bundle/release/app-release.aab
```

**Why AAB?**
- Smaller download size for users
- Google Play's dynamic delivery
- Required for apps over 150MB
- Better optimization

---

## 3. Google Play Store Deployment

### Prerequisites

1. **Google Play Developer Account** ($25 one-time fee)
   - Register at: https://play.google.com/console/signup

2. **Prepare App Assets**
   - App icon (512x512 PNG)
   - Feature graphic (1024x500 JPG/PNG)
   - Screenshots (min 2, max 8)
     - Phone: 16:9 or 9:16 aspect ratio
     - Tablet: 16:9 or 9:16 aspect ratio
   - Promotional video (optional, YouTube link)
   - Privacy policy URL

### Step-by-Step Deployment

#### Step 1: Create Application

1. Go to [Google Play Console](https://play.google.com/console)
2. Click "Create app"
3. Fill in details:
   - **App name**: Startup Showcase
   - **Default language**: English (United States)
   - **App or game**: App
   - **Free or paid**: Free
4. Accept declarations and click "Create app"

#### Step 2: Set Up Store Listing

Navigate to "Store listing" and fill in:

**App details:**
- **Short description** (max 80 characters):
```
Showcase your startup! Create profiles, share videos, discover innovations.
```

- **Full description** (max 4000 characters):
```
Startup Showcase is the ultimate platform for startups, small businesses, and enterprises to showcase their innovations, products, and services to a global audience.

🚀 KEY FEATURES:

📱 Company Profiles
Create comprehensive business profiles with logos, descriptions, product images, videos, and contact information. Showcase what makes your company unique!

🎬 Reels/Video Section
Upload short promotional videos similar to social media reels, but focused exclusively on innovations, startups, and new ideas. Let your creativity shine!

🔍 Discover & Explore
Browse companies by 19+ industry categories including Technology, Healthcare, FinTech, E-commerce, and more. Find exactly what you're looking for with advanced search and filters.

⭐ Featured Companies
Stand out from the crowd with featured placement and verified badges. Get the visibility your startup deserves!

📊 Analytics Dashboard
Track your performance with detailed analytics including profile views, engagement rates, follower growth, and top-performing content.

💼 For Business Owners:
- Create and manage company profiles
- Upload promotional videos (Reels)
- Track analytics and insights
- Engage with followers
- Showcase products and services

👥 For Explorers:
- Discover innovative startups
- Watch inspiring company stories
- Follow your favorite businesses
- Save companies for later
- Search by industry category

Whether you're a startup founder, business owner, or innovation enthusiast, Startup Showcase connects you with the entrepreneurial ecosystem like never before.

Download now and join the revolution! 🚀
```

- **App icon**: Upload 512x512 PNG
- **Feature graphic**: Upload 1024x500 image
- **Phone screenshots**: Upload 2-8 screenshots
- **Tablet screenshots**: Upload if you have tablet-optimized layouts

**Categorization:**
- **App category**: Business
- **Tags**: startup, business, innovation, entrepreneur, company profiles

**Contact details:**
- Email: your-email@example.com
- Website: https://your-website.com
- Phone: +1-234-567-8900 (optional)

**Privacy Policy:**
- URL: https://your-website.com/privacy-policy
  *(You must create and host a privacy policy)*

#### Step 3: Configure App Content

1. **App access**
   - All functionality is available without restrictions: Yes/No
   - If restricted, provide test credentials

2. **Ads**
   - Does your app contain ads? No
   - *(Set to Yes if you plan to add ads later)*

3. **Content ratings**
   - Complete questionnaire
   - Most answers will be "No" for a business showcase app
   - Typical rating: Everyone or Teen

4. **Target audience**
   - Age range: 13+ or 18+
   - Comply with relevant child protection laws

5. **News app**
   - Is this a news app? No

6. **COVID-19 contact tracing**
   - Is this a COVID-19 contact tracing app? No

7. **Data safety**
   - Fill out data safety form:
     - What data is collected?
     - How is it used?
     - Is it shared with third parties?
     - Security practices

**Example Data Safety Declaration:**
```
Data collected:
- Email address (for authentication)
- Name (for user profile)
- Company information (if company owner)

Data usage:
- App functionality
- Account management
- Analytics

Data sharing: No third-party sharing

Security:
- Data encrypted in transit
- User can request data deletion
- Firebase security best practices
```

#### Step 4: Set Up Pricing & Distribution

1. **Countries/regions**
   - Select "Add countries/regions"
   - Choose: All countries or specific countries

2. **Pricing**
   - Free (or set a price if premium)

3. **Program opt-in**
   - Google Play for Education: Optional
   - Designed for Families: Optional (if appropriate)

#### Step 5: Production Release

1. Navigate to "Production" → "Releases"
2. Click "Create new release"
3. **App integrity:**
   - Use Google Play App Signing: Yes (Recommended)
   - Let Google manage your app signing key

4. **Upload app bundle:**
   - Click "Upload"
   - Select `app/build/outputs/bundle/release/app-release.aab`
   - Wait for upload and processing

5. **Release details:**
   - **Release name**: Version 1.0.0 (or follow your versioning)
   - **Release notes** (What's new):
```
🎉 Welcome to Startup Showcase v1.0.0!

✨ Features in this release:
• Create comprehensive company profiles
• Upload and watch promotional video reels
• Browse companies by industry category
• Follow your favorite startups
• View analytics and insights
• Search and discover innovations
• Featured companies section
• User authentication and profiles

We're excited to help you showcase your startup to the world! 🚀
```

6. Click "Review release"
7. Check for any warnings or errors
8. Click "Start rollout to Production"

#### Step 6: Monitoring

After submission:
- **Review time**: Usually 1-7 days
- **Status**: Check "Publishing overview" for status
- **Notifications**: You'll receive emails about review status

---

## 4. Firebase App Distribution (Beta Testing)

Perfect for testing before Play Store release.

### Step 1: Setup Firebase App Distribution

```bash
# Install Firebase CLI
npm install -g firebase-tools

# Login to Firebase
firebase login

# Initialize Firebase in your project
cd /workspaces/Startup
firebase init
```

Select:
- App Distribution
- Use existing project
- Select your Firebase project

### Step 2: Configure Gradle Plugin

Add to `app/build.gradle.kts`:

```kotlin
plugins {
    // ... existing plugins ...
    id("com.google.firebase.appdistribution") version "4.0.1"
}

android {
    // ... existing config ...
    
    buildTypes {
        release {
            // ... existing config ...
            
            firebaseAppDistribution {
                releaseNotes = "Beta release - Test new features!"
                groups = "beta-testers"
            }
        }
    }
}
```

### Step 3: Build and Distribute

```bash
# Build and upload to Firebase App Distribution
./gradlew assembleRelease appDistributionUploadRelease

# Or for specific groups
./gradlew assembleRelease \
  appDistributionUploadRelease \
  --groups "qa-team,beta-testers"
```

### Step 4: Manage Testers

1. Go to [Firebase Console](https://console.firebase.google.com)
2. Select your project
3. Navigate to "App Distribution"
4. Add testers by email or create groups
5. Testers receive email with download link

**Benefits:**
- Quick distribution to testers
- Automatic notifications
- Feedback collection
- Version management
- No Play Store review needed

---

## 5. Alternative Distribution Methods

### A. Direct APK Distribution

**Use cases:** Internal testing, enterprise deployment

1. Build release APK (see Section 2)
2. Host APK on:
   - Your website
   - Cloud storage (Google Drive, Dropbox)
   - Internal server

**User installation:**
1. Download APK from your source
2. Enable "Install from unknown sources" in Android settings
3. Open APK file to install

**⚠️ Important:** Inform users about security implications

### B. Amazon Appstore

1. Register at [Amazon Appstore Developer Console](https://developer.amazon.com/apps-and-games/console)
2. Create new app
3. Upload APK
4. Fill in metadata (similar to Play Store)
5. Submit for review

### C. Samsung Galaxy Store

1. Register at [Samsung Seller Portal](https://seller.samsungapps.com/)
2. Create new application
3. Upload APK/AAB
4. Complete metadata
5. Submit for review

### D. Your Own Website

Create a download page:

```html
<!DOCTYPE html>
<html>
<head>
    <title>Download Startup Showcase</title>
</head>
<body>
    <h1>Download Startup Showcase</h1>
    <p>Version 1.0.0</p>
    <a href="app-release.apk" download>
        <button>Download APK</button>
    </a>
    <p>
        <strong>Installation Instructions:</strong><br>
        1. Download the APK<br>
        2. Enable "Install from unknown sources"<br>
        3. Open the APK to install
    </p>
</body>
</html>
```

---

## 6. CI/CD Pipeline Setup

### GitHub Actions (Recommended)

Create `.github/workflows/deploy.yml`:

```yaml
name: Android CI/CD

on:
  push:
    branches: [ main ]
  pull_request:
    branches: [ main ]

jobs:
  build:
    runs-on: ubuntu-latest
    
    steps:
    - uses: actions/checkout@v3
    
    - name: Set up JDK 17
      uses: actions/setup-java@v3
      with:
        java-version: '17'
        distribution: 'temurin'
        
    - name: Grant execute permission for gradlew
      run: chmod +x gradlew
      
    - name: Build with Gradle
      run: ./gradlew build
      
    - name: Run tests
      run: ./gradlew test
      
    - name: Build Release APK
      run: ./gradlew assembleRelease
      
    - name: Upload APK
      uses: actions/upload-artifact@v3
      with:
        name: app-release
        path: app/build/outputs/apk/release/app-release.apk

  deploy:
    needs: build
    runs-on: ubuntu-latest
    if: github.ref == 'refs/heads/main'
    
    steps:
    - uses: actions/checkout@v3
    
    - name: Deploy to Firebase App Distribution
      uses: wzieba/Firebase-Distribution-Github-Action@v1
      with:
        appId: ${{ secrets.FIREBASE_APP_ID }}
        token: ${{ secrets.FIREBASE_TOKEN }}
        groups: beta-testers
        file: app/build/outputs/apk/release/app-release.apk
```

**Setup GitHub Secrets:**
1. Go to your GitHub repo
2. Settings → Secrets and variables → Actions
3. Add secrets:
   - `FIREBASE_APP_ID`: Your Firebase app ID
   - `FIREBASE_TOKEN`: Run `firebase login:ci` to get token

---

## 7. Post-Deployment Checklist

### Immediate Actions

- [ ] **Monitor Crash Reports**
  - Firebase Crashlytics dashboard
  - Google Play Console vitals

- [ ] **Check Analytics**
  - User engagement
  - Screen views
  - User retention

- [ ] **Review User Feedback**
  - Play Store reviews
  - In-app feedback
  - Social media mentions

### Week 1

- [ ] **Fix Critical Bugs**
  - Address crash reports
  - Fix high-priority issues
  
- [ ] **Monitor Performance**
  - App load time
  - Video playback
  - Network requests

- [ ] **User Support**
  - Respond to reviews
  - Answer support emails

### Ongoing

- [ ] **Regular Updates**
  - Release updates every 2-4 weeks
  - Add new features
  - Improve performance

- [ ] **A/B Testing**
  - Test new features with subset of users
  - Optimize user experience

- [ ] **Marketing**
  - Social media promotion
  - Content marketing
  - App Store Optimization (ASO)

---

## Troubleshooting Common Issues

### Issue: "Keystore was tampered with"
```bash
# Solution: Verify keystore password
keytool -list -v -keystore startup-showcase-release.keystore
```

### Issue: "APK signature verification failed"
```bash
# Solution: Rebuild with correct signing config
./gradlew clean
./gradlew assembleRelease
```

### Issue: "App not compatible with device"
```bash
# Solution: Check minSdkVersion in build.gradle
# Ensure device meets minimum requirements (API 24+)
```

### Issue: Play Console upload fails
- **Solution**: Ensure AAB file is under 150MB
- Reduce resources, enable R8 shrinking
- Check for ProGuard errors

---

## Security Best Practices

1. **Never commit keystore files**
   - Add to `.gitignore`
   - Store securely (password manager, encrypted storage)

2. **Use environment variables for secrets**
   - CI/CD secrets
   - API keys
   - Firebase config

3. **Enable ProGuard/R8**
   - Code obfuscation
   - Resource shrinking
   - Security through obscurity

4. **Implement certificate pinning**
   - Prevent MITM attacks
   - Add to OkHttp configuration

---

## Need Help?

- **Google Play Console Help**: https://support.google.com/googleplay/android-developer
- **Firebase Documentation**: https://firebase.google.com/docs
- **Android Developer Guide**: https://developer.android.com/studio/publish

---

**Good luck with your deployment! 🚀**
