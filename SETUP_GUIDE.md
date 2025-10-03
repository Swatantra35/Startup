# Startup Showcase - Setup Guide

## Complete Setup Instructions

### 1. Environment Setup

#### Install Required Tools
- **Android Studio**: Download from [developer.android.com](https://developer.android.com/studio)
- **JDK 17**: Included with Android Studio
- **Git**: For version control

### 2. Firebase Setup

#### Step 1: Create Firebase Project
1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Click "Add Project"
3. Enter project name: `startup-showcase-app`
4. Disable Google Analytics (or enable if you want)
5. Click "Create Project"

#### Step 2: Add Android App
1. In Firebase Console, click "Add app" → Android icon
2. Enter package name: `com.startupshowcase.app`
3. Enter app nickname: "Startup Showcase"
4. Download `google-services.json`
5. Place it in `app/` directory

#### Step 3: Enable Authentication
1. In Firebase Console, go to "Authentication"
2. Click "Get Started"
3. Enable these sign-in methods:
   - **Email/Password**: Click "Enable" → Save
   - **Google**: 
     - Click "Enable"
     - Enter support email
     - Download the configuration
     - Save

#### Step 4: Setup Firestore Database
1. In Firebase Console, go to "Firestore Database"
2. Click "Create Database"
3. Choose "Start in test mode" (for development)
4. Select your region
5. Click "Enable"

**Security Rules** (Update after testing):
```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    // Companies collection
    match /companies/{companyId} {
      allow read: if true;
      allow create: if request.auth != null;
      allow update, delete: if request.auth.uid == resource.data.ownerId;
    }
    
    // Reels collection
    match /reels/{reelId} {
      allow read: if resource.data.isActive == true;
      allow create: if request.auth != null;
      allow update, delete: if request.auth.uid == resource.data.ownerId;
    }
    
    // Users collection
    match /users/{userId} {
      allow read: if true;
      allow write: if request.auth.uid == userId;
    }
  }
}
```

#### Step 5: Setup Cloud Storage
1. In Firebase Console, go to "Storage"
2. Click "Get Started"
3. Choose "Start in test mode"
4. Click "Next" → "Done"

**Storage Rules** (Update after testing):
```javascript
rules_version = '2';
service firebase.storage {
  match /b/{bucket}/o {
    match /reels/{allPaths=**} {
      allow read: if true;
      allow write: if request.auth != null
                   && request.resource.size < 50 * 1024 * 1024
                   && request.resource.contentType.matches('video/.*');
    }
    
    match /images/{allPaths=**} {
      allow read: if true;
      allow write: if request.auth != null
                   && request.resource.size < 5 * 1024 * 1024
                   && request.resource.contentType.matches('image/.*');
    }
  }
}
```

#### Step 6: Enable Cloud Messaging (FCM)
1. In Firebase Console, go to "Cloud Messaging"
2. Note down your Server Key for push notifications

### 3. Project Configuration

#### Clone and Open Project
```bash
git clone https://github.com/Swatantra35/Startup.git
cd Startup
```

Open in Android Studio:
- File → Open → Select the `Startup` folder

#### Configure google-services.json
- Ensure `google-services.json` is in `app/` directory
- The file should contain your actual Firebase project credentials

#### Sync Gradle
- Android Studio will automatically detect and sync Gradle
- If not, click "Sync Now" or File → Sync Project with Gradle Files

### 4. Build Configuration

#### Update build.gradle (if needed)
The project uses:
- Compile SDK: 34
- Min SDK: 24
- Target SDK: 34
- Kotlin: 1.9.20

#### Sign the App (for Release)
1. Create a keystore:
```bash
keytool -genkey -v -keystore startup-showcase.keystore -alias startup -keyalg RSA -keysize 2048 -validity 10000
```

2. Add to `app/build.gradle.kts`:
```kotlin
android {
    signingConfigs {
        create("release") {
            storeFile = file("path/to/startup-showcase.keystore")
            storePassword = "your-password"
            keyAlias = "startup"
            keyPassword = "your-password"
        }
    }
    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("release")
        }
    }
}
```

### 5. Running the App

#### Debug Build
1. Connect Android device via USB or start emulator
2. Enable USB Debugging on device
3. Click "Run" (Shift + F10) in Android Studio

#### Release Build
```bash
./gradlew assembleRelease
```
APK will be in: `app/build/outputs/apk/release/`

### 6. Testing

#### Create Test Data
Use Firebase Console to add sample companies and reels:

**Sample Company Document** (Firestore):
```json
{
  "name": "TechStart Innovations",
  "tagline": "Building the future of AI",
  "description": "We create cutting-edge AI solutions for businesses",
  "logo": "https://example.com/logo.png",
  "coverImage": "https://example.com/cover.jpg",
  "industry": "TECHNOLOGY",
  "companySize": "STARTUP",
  "foundedYear": 2023,
  "website": "https://techstart.com",
  "email": "info@techstart.com",
  "phone": "+1234567890",
  "isFeatured": true,
  "isVerified": true,
  "followers": 1250,
  "views": 5420,
  "createdAt": 1704067200000
}
```

**Sample Reel Document** (Firestore):
```json
{
  "companyId": "company-id-here",
  "companyName": "TechStart Innovations",
  "companyLogo": "https://example.com/logo.png",
  "title": "Our New AI Product Launch",
  "description": "Revolutionizing the industry with AI",
  "videoUrl": "https://example.com/video.mp4",
  "thumbnailUrl": "https://example.com/thumb.jpg",
  "duration": 30,
  "category": "PRODUCT_LAUNCH",
  "likes": 450,
  "views": 12500,
  "shares": 89,
  "isActive": true,
  "createdAt": 1704067200000
}
```

### 7. Troubleshooting

#### Common Issues

**Issue: google-services.json not found**
- Solution: Download from Firebase Console and place in `app/` directory

**Issue: Build fails with dependency conflicts**
- Solution: File → Invalidate Caches → Invalidate and Restart

**Issue: Firebase authentication not working**
- Solution: Ensure SHA-1 fingerprint is added in Firebase Console
```bash
./gradlew signingReport
```

**Issue: Videos not playing**
- Solution: Ensure ExoPlayer dependencies are properly added and internet permission is granted

**Issue: Images not loading**
- Solution: Check internet connection and Glide configuration

### 8. Deployment

#### Google Play Store
1. Create a Google Play Developer account
2. Generate signed release APK/AAB
3. Create app listing
4. Upload release build
5. Complete store listing
6. Submit for review

#### Firebase App Distribution (Beta Testing)
```bash
./gradlew assembleRelease appDistributionUploadRelease
```

### 9. Monitoring & Analytics

#### Enable Firebase Analytics
- Already enabled via Firebase SDK
- View reports in Firebase Console → Analytics

#### Crashlytics (Optional)
Add to `app/build.gradle.kts`:
```kotlin
plugins {
    id("com.google.firebase.crashlytics") version "2.9.9"
}

dependencies {
    implementation("com.google.firebase:firebase-crashlytics-ktx")
}
```

### 10. Next Steps

1. **Customize branding**: Update colors, logos, and app name
2. **Add more features**: Implement the roadmap items
3. **Improve security**: Update Firebase rules for production
4. **Performance**: Enable ProGuard and optimize images
5. **Localization**: Add support for multiple languages

## Support

For issues or questions:
- Create an issue on GitHub
- Email: support@startupshowcase.com

## Resources

- [Android Developer Docs](https://developer.android.com/)
- [Firebase Documentation](https://firebase.google.com/docs)
- [Kotlin Documentation](https://kotlinlang.org/docs/home.html)
- [Material Design 3](https://m3.material.io/)
