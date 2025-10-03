# Quick Start Guide

## Deploy Your App in 3 Simple Steps!

### Step 1: Setup Firebase
1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Create a new project
3. Add an Android app with package: `com.startupshowcase.app`
4. Download `google-services.json` to `app/` directory
5. Enable Authentication, Firestore, and Storage

### Step 2: Build the App

**For Testing (Debug):**
```bash
chmod +x build.sh
./build.sh
# Choose option 1 (Debug Build)
# Or option 6 (Install on device)
```

**For Production (Release):**
```bash
# First, create a keystore (one-time setup)
chmod +x create-keystore.sh
./create-keystore.sh

# Then build release version
./build.sh
# Choose option 2 (APK) or 3 (AAB for Play Store)
```

### Step 3: Deploy

**Option A: Google Play Store** (Recommended)
- Use the AAB file from Step 2
- Follow the detailed guide in [DEPLOYMENT_GUIDE.md](DEPLOYMENT_GUIDE.md#3-google-play-store-deployment)

**Option B: Firebase App Distribution** (Beta Testing)
```bash
npm install -g firebase-tools
firebase login
firebase init
./gradlew assembleRelease appDistributionUploadRelease
```

**Option C: Direct Distribution**
- Share the APK file directly
- Users must enable "Install from unknown sources"

## What's Next?

📖 **Full Documentation:**
- [SETUP_GUIDE.md](SETUP_GUIDE.md) - Complete Firebase setup
- [DEPLOYMENT_GUIDE.md](DEPLOYMENT_GUIDE.md) - All deployment methods
- [API_DOCUMENTATION.md](API_DOCUMENTATION.md) - Database structure

🛠️ **Customize:**
- Update colors in `app/src/main/res/values/colors.xml`
- Change app name in `app/src/main/res/values/strings.xml`
- Replace app icon in `app/src/main/res/mipmap/`

🚀 **Start Developing:**
```bash
# Open in Android Studio
# File → Open → Select Startup folder
# Click Run (Shift + F10)
```

## Need Help?

- Check [DEPLOYMENT_GUIDE.md](DEPLOYMENT_GUIDE.md) for detailed instructions
- Common issues and solutions are documented
- Create an issue on GitHub if you're stuck

**Good luck! 🎉**
