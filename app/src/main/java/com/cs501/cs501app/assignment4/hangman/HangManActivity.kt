package com.cs501.cs501app.assignment4.hangman

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import androidx.activity.viewModels
import com.cs501.cs501app.R

private const val TAG = "HangManActivity"

class HangManActivity : AppCompatActivity() {
    private val backend by viewModels<HMBackend>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hang_man)
        val fragment = HintFragment()
        val hintText = backend.getHint()
        val avaliableChars = backend.getAvailableChars()
        savedInstanceState?.putString("hintText", hintText)
        savedInstanceState?.putCharArray("availableChars", avaliableChars)

        fragment.arguments = savedInstanceState
        // need get backend returns for hintfragment
        // this would cause hintfragment to init twice, use this for imagefragment
        supportFragmentManager.beginTransaction()
            .add(R.id.hintFragment, fragment)
            .commit()
    }
//
//    override fun onButtonStateChanged(buttonStates: Map<String, Boolean>) {
//        // Pass the button state map from the InputFragment to the HintFragment
//        val hintFragment = supportFragmentManager.findFragmentById(R.id.hintFragment) as HintFragment
//        hintFragment.onButtonStateChanged(buttonStates)
//
//    }
//
//    override fun onLetterButtonsContainerCreated(container: ViewGroup) {
//        supportFragmentManager.findFragmentById(R.id.hintFragment)?.let {
//            if (it is HintFragment) {
//                it.onLetterButtonsContainerCreated(container)
//            }
//        }
//    }

}
