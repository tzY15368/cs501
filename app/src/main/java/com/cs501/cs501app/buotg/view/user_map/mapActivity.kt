package com.cs501.cs501app.buotg.view.user_map

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview


class mapActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                showMap()
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Preview
    @Composable
    fun showMap() {
        //you can change the strFrom and strTo to any address you want, include longitude and latitude like this(a,b)
        val strFrom = "Boston, MA"
        val strTo = "New York, NY"
        val uri = "http://maps.google.com/maps?f=d&saddr=$strFrom,&daddr=$strTo&hl=en"
        val intent = Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri))
        startActivity(intent)
    }

}