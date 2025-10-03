package com.startupshowcase.app.util

object Constants {
    
    // Firebase Collections
    const val COLLECTION_COMPANIES = "companies"
    const val COLLECTION_USERS = "users"
    const val COLLECTION_REELS = "reels"
    const val COLLECTION_ANALYTICS = "analytics"
    
    // Shared Preferences
    const val PREF_NAME = "startup_showcase_prefs"
    const val PREF_USER_ID = "user_id"
    const val PREF_USER_TYPE = "user_type"
    
    // Pagination
    const val PAGE_SIZE = 20
    const val INITIAL_LOAD_SIZE = 40
    
    // Video Constraints
    const val MAX_REEL_DURATION = 60 // seconds
    const val MIN_REEL_DURATION = 5  // seconds
    const val MAX_VIDEO_SIZE_MB = 50
    
    // Image Constraints
    const val MAX_IMAGE_SIZE_MB = 5
    const val MAX_GALLERY_IMAGES = 10
    
    // Cache
    const val CACHE_DURATION_MINUTES = 30
    
    // Animation Durations
    const val ANIMATION_DURATION_SHORT = 200L
    const val ANIMATION_DURATION_MEDIUM = 300L
    const val ANIMATION_DURATION_LONG = 500L
    
    // Delay
    const val SEARCH_DELAY_MS = 500L
    const val SPLASH_DELAY_MS = 2000L
}
