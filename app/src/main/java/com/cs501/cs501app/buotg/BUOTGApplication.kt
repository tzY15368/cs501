package com.cs501.cs501app.buotg

import android.app.Application
import com.cs501.cs501app.buotg.database.repositories.AppRepository

class BUOTGApplication:Application() {
    override fun onCreate() {
        super.onCreate()
        AppRepository.initialize(this)
    }
}