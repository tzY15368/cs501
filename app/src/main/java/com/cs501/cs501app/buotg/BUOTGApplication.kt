package com.cs501.cs501app.buotg

import android.app.Application
import androidx.activity.compose.setContent
import com.cs501.cs501app.buotg.database.AppRepository
import com.cs501.cs501app.buotg.dayNightTheme.EventTrackerTheme

class BUOTGApplication:Application() {
    override fun onCreate() {
        super.onCreate()
        AppRepository.initialize(this)
    }
}