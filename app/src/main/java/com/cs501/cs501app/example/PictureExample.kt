package com.cs501.cs501app.example

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.cs501.cs501app.R

class PictureExample: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContent{
            showPicture(1)
        }
    }

    @Composable
    private fun showPicture(number: Int) {
        var which = R.drawable.step0;
        which = when(number){
            0 -> R.drawable.step0
            1 -> R.drawable.step1
            2 -> R.drawable.step2
            3 -> R.drawable.step3
            4 -> R.drawable.step4
            5 -> R.drawable.step5
            6 -> R.drawable.step6
            else -> R.drawable.step0
        }

        Image(painter = painterResource(id = which),
            contentDescription = null)
    }

    @Preview
    @Composable
    fun PreShowPicture(){
        showPicture(2)
    }
}