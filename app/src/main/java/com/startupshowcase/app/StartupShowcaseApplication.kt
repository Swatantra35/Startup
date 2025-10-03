package com.startupshowcase.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class StartupShowcaseApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        instance = this
    }
    
    companion object {
        private lateinit var instance: StartupShowcaseApplication
        
        fun getInstance(): StartupShowcaseApplication {
            return instance
        }
    }
}
