package com.cs501.cs501app.assignment4.hangman

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.cs501.cs501app.R

private const val TAG = "HangManActivity"
class HangManActivity : AppCompatActivity(), HintFragmentListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hang_man)
        val fragment = HintFragment()
        // this would cause hintfragment to init twice, use this for imagefragment
//        supportFragmentManager.beginTransaction()
//            .add(R.id.fragment_container, fragment)
//            .commit()
    }

    override fun onHintPressed() {
        Log.d(TAG, "onHintPressed: ")
    }


}