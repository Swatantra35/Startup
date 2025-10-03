# Startup Showcase - Android Mobile Application

A modern, interactive Android mobile application built with **Kotlin** that allows ## 🚀 Deployment

### 📍 Ready to Deploy?

**👉 [START HERE - Complete Deployment Guide](START_HERE.md)**

### Quick Links

| What You Need | Where to Go |
|---------------|-------------|
| 🎯 **First time deploying?** | [START_HERE.md](START_HERE.md) |
| ⚡ **Want the fastest path?** | [QUICKSTART.md](QUICKSTART.md) |
| 📖 **Need detailed guide?** | [DEPLOYMENT_GUIDE.md](DEPLOYMENT_GUIDE.md) |
| 📊 **Visual flowcharts?** | [DEPLOYMENT_FLOWCHART.md](DEPLOYMENT_FLOWCHART.md) |
| 📚 **See all deployment docs?** | [DEPLOY_INDEX.md](DEPLOY_INDEX.md) |

### 🛠️ Quick Commands

```bash
# Check if ready to deploy
./check-deployment.sh

# Interactive build menu
./build.sh

# Create release keystore (one-time setup)
./create-keystore.sh
```

**Deployment Methods:**
- 🏪 **Google Play Store** - Reach 2B+ users (Recommended)
- 🧪 **Firebase App Distribution** - Beta testing
- 📦 **Direct APK** - Internal/Enterprise useusinesses to showcase their products, services, and innovations through profiles and short-form video content (Reels).

## 📱 Features

### Core Features
- **Company Profiles**: Create comprehensive business profiles with logos, descriptions, product images, videos, and contact information
- **Reels/Video Section**: Upload and view short promotional videos (similar to Instagram Reels) focused on innovations, startups, and new ideas
- **Industry Categories**: Browse companies by 19+ industry categories
- **Search & Filter**: Advanced search and filtering capabilities
- **Featured Ads**: Highlight companies with featured placement
- **Company Dashboard**: Manage listings, videos, and view analytics
- **Follow System**: Follow your favorite companies and get updates
- **Analytics Dashboard**: Track profile views, engagement, and performance metrics

### User Types
- **Visitors**: Browse companies and watch reels
- **Company Owners**: Create and manage company profiles and content
- **Admins**: Platform administration capabilities

## 🏗️ Architecture

This app follows **Clean Architecture** principles with MVVM pattern:

```
app/
├── data/                    # Data layer
│   └── repository/         # Repository implementations
├── domain/                  # Business logic layer
│   ├── model/              # Domain models
│   └── repository/         # Repository interfaces
├── presentation/            # UI layer
│   ├── home/               # Home screen
│   ├── reels/              # Reels player
│   ├── search/             # Search functionality
│   ├── company/            # Company details
│   ├── dashboard/          # Analytics dashboard
│   └── auth/               # Authentication
└── di/                      # Dependency injection
```

## 🛠️ Tech Stack

### Core Technologies
- **Language**: Kotlin
- **Min SDK**: 24 (Android 7.0)
- **Target SDK**: 34 (Android 14)

### Architecture Components
- **MVVM**: Model-View-ViewModel architecture
- **Clean Architecture**: Separation of concerns
- **Jetpack Compose**: Modern declarative UI (alongside XML)
- **Navigation Component**: Fragment navigation
- **ViewBinding & DataBinding**: Type-safe view access

### Dependency Injection
- **Hilt (Dagger)**: Dependency injection framework

### Backend & Cloud
- **Firebase Authentication**: User authentication
- **Firebase Firestore**: Real-time database
- **Firebase Storage**: Video and image storage
- **Firebase Cloud Messaging**: Push notifications
- **Firebase Analytics**: User analytics

### Networking
- **Retrofit**: RESTful API calls
- **OkHttp**: HTTP client
- **Gson**: JSON serialization

### Media & UI
- **ExoPlayer**: Video playback for reels
- **Glide & Coil**: Image loading
- **Lottie**: Smooth animations
- **Material Design 3**: Modern UI components
- **Shimmer**: Loading placeholders

### Data Persistence
- **Room Database**: Local caching
- **DataStore**: Preferences storage

### Concurrency
- **Kotlin Coroutines**: Asynchronous programming
- **Flow**: Reactive streams

### Other Libraries
- **MPAndroidChart**: Analytics charts
- **Paging 3**: Efficient data loading
- **ViewPager2**: Swipeable reels

## 🚀 Getting Started

### Prerequisites
- Android Studio Hedgehog | 2023.1.1 or later
- JDK 17
- Android SDK with API 34
- Firebase account

### Setup Instructions

1. **Clone the repository**
```bash
git clone https://github.com/Swatantra35/Startup.git
cd Startup
```

2. **Configure Firebase**
   - Create a new Firebase project at [Firebase Console](https://console.firebase.google.com/)
   - Add an Android app with package name: `com.startupshowcase.app`
   - Download `google-services.json` and place it in `app/` directory
   - Enable the following in Firebase:
     - Authentication (Email/Password & Google Sign-In)
     - Firestore Database
     - Cloud Storage
     - Cloud Messaging
     - Analytics

3. **Update Firebase Configuration**
   - Replace the placeholder values in `app/google-services.json` with your actual Firebase configuration

4. **Sync Gradle**
   - Open the project in Android Studio
   - Let Gradle sync automatically
   - Resolve any dependency issues

5. **Run the App**
   - Connect an Android device or start an emulator
   - Click "Run" or press `Shift + F10`

For detailed setup instructions, see [SETUP_GUIDE.md](SETUP_GUIDE.md)

## � Quick Deploy

**Want to deploy quickly?** See [QUICKSTART.md](QUICKSTART.md) for a 3-step deployment guide!

For comprehensive deployment instructions including Google Play Store, Firebase App Distribution, and CI/CD setup, check out [DEPLOYMENT_GUIDE.md](DEPLOYMENT_GUIDE.md).

**Build Scripts Available:**
```bash
# Interactive build menu
./build.sh

# Create release keystore (one-time setup)
./create-keystore.sh
```

## �📊 Database Structure

### Firestore Collections

**companies**
- Company profiles with all business information
- Subcollections: products, reviews

**users**
- User profiles and preferences
- Authentication data

**reels**
- Short video content
- Engagement metrics (likes, views, shares)

**analytics**
- Company performance metrics
- User engagement data

## 🎨 UI/UX Features

- **Modern Material Design 3**: Latest design guidelines
- **Smooth Animations**: Lottie & custom animations
- **Dark Mode Support**: System-adaptive theming
- **Responsive Layouts**: Optimized for all screen sizes
- **Gesture Support**: Swipe, tap, long-press interactions
- **Loading States**: Shimmer effects and progress indicators
- **Error Handling**: User-friendly error messages

## 🔒 Security Features

- Firebase Security Rules for data access control
- ProGuard configuration for code obfuscation
- Secure credential storage
- HTTPS-only communication
- Input validation and sanitization

## 📈 Performance Optimizations

- Image caching and compression
- Lazy loading with Paging 3
- Video streaming optimization
- Room database for offline support
- Efficient RecyclerView with DiffUtil

## 🧪 Testing

```bash
# Run unit tests
./gradlew test

# Run instrumented tests
./gradlew connectedAndroidTest
```

## 📱 App Features in Detail

### Home Screen
- Featured companies carousel
- Category filter chips
- Company listings with follow functionality
- Pull-to-refresh support

### Reels Section
- Vertical swipe video player (TikTok/Instagram style)
- Auto-play with mute/unmute
- Like, comment, share actions
- View count tracking
- Company profile quick access

### Company Profile
- Detailed company information
- Product showcase gallery
- Services listing
- Contact information
- Social media links
- Company reels collection

### Dashboard (For Company Owners)
- Analytics overview
- Profile view statistics
- Engagement metrics
- Top-performing reels
- Follower growth charts
- Edit company profile
- Upload new reels

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👨‍💻 Author

**Swatantra35**
- GitHub: [@Swatantra35](https://github.com/Swatantra35)

## 🙏 Acknowledgments

- Material Design Icons
- Firebase team for excellent documentation
- Android Jetpack libraries
- All open-source contributors

## 📞 Support

For support, email support@startupshowcase.com or open an issue in this repository.

## 🗺️ Roadmap

- [ ] Add real-time chat between users and companies
- [ ] Implement AI-powered company recommendations
- [ ] Add multi-language support
- [ ] Integrate payment gateway for premium features
- [ ] Add live streaming capability
- [ ] Implement AR product previews
- [ ] Add job posting functionality
- [ ] Create web dashboard for companies

---

**Made with ❤️ for the startup community**