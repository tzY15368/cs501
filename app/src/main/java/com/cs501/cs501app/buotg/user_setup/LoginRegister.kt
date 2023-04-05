package com.cs501.cs501app.buotg.user_setup

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun LoginRegister(done:()->Unit = {}, msg:String=""){
    Log.d("LoginRegister", "rendered: msg=$msg")
    // add the clickable modifier
    Text(text = "LoginRegister", modifier = Modifier.height(50.dp).clickable {
        Log.d("LoginRegister", "clicked")
        done()

    })
}