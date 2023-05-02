package com.cs501.cs501app.buotg.view.user_map

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text

class TestAddrPickerActivity : AppCompatActivity() {

    private val viewModel: MapViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Row(){
                Column(){
                    Text("Hello")
                }
            }
            MapAddressPickerView(viewModel)
        }
    }
}