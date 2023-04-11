package com.cs501.cs501app.assignment4.hangman

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.cs501.cs501app.R
import com.cs501.cs501app.utils.TAlert

private const val TAG = "HangManActivity"

class HangManActivity : AppCompatActivity() {
    private lateinit var viewModel: HMBackend

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hang_man)
        val fragment = ChooseLetterFragment()
        viewModel = ViewModelProvider(this).get(HMBackend::class.java)
        // need get backend returns for hintfragment
        // this would cause hintfragment to init twice, use this for imagefragment
//        supportFragmentManager.beginTransaction()
//            .add(R.id.hangmen, fragment)
//            .commit()
        viewModel.gameState.observe(this, Observer{
            Log.d(TAG, "game state did change")
            TAlert.success(this, "Game state changed to $it")
        })
    }
}
