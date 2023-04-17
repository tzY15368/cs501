package com.cs501.cs501app.buotg

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import com.cs501.cs501app.buotg.view.dayNightTheme.EventTrackerTheme
import com.cs501.cs501app.buotg.view.homeScreen.EventTracker

class HomeActivity : AppCompatActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{
            EventTrackerTheme {
                EventTracker()
            }
        }
    }
}