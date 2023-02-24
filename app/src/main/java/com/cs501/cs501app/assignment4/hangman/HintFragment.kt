package com.cs501.cs501app.assignment4.hangman

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import com.cs501.cs501app.R
import com.cs501.cs501app.assignment3.flashcard.FCBackend
import com.cs501.cs501app.databinding.FragmentHintBinding
import kotlin.system.exitProcess

private const val TAG = "HintFragment"

class HintFragment : Fragment(){

//    private lateinit var listener: ButtonStateListener
    private val hmBackend: HMBackend by activityViewModels()

    private var _binding: FragmentHintBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }

    private var hintCounter = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(TAG, "onAttach: ")
//        if (context is ButtonStateListener) {
//            listener = context
//            Log.d(TAG, "onAttach: ")
//            listener.onButtonStateChanged(buttonStates)
//        } else {
//            throw RuntimeException("$context must implement HintFragmentListener")
//        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHintBinding.inflate(inflater, container, false)
        binding.hintButton.setOnClickListener(View.OnClickListener {
                if (hintCounter == 0) {
                    Log.d(TAG, "This is your first hint!")
                    changeHintText(hmBackend.getHint())
                } else if (hintCounter == 1) {
                    disableHalfLetters()
                    Log.d(TAG, "Half of the letters have been disabled.")
                } else if (hintCounter == 2) {
                    showVowels()
                    Log.d(TAG, "All the vowels have been shown.")
                } else {
                    Log.d(TAG, "Hint not available.")
                    binding.hintButton.isEnabled = false
//                    exitProcess(0)
                    //TODO: How to exit the game after failing?
                }
                hintCounter++
            }
        )

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        println("destroyed")
    }
    override fun onDestroyView() {
        super.onDestroyView()
        println("destroyed view")
    }


    fun changeHintText(text: String) {
        binding.hintText.text = text
    }

    private fun disableHalfLetters() {
        val remainingLetters = hmBackend.getAvailableChars()
        val half = remainingLetters.size / 2
        val currentRemainingLetters = mutableListOf<Char>()
        for (i in 0 until half) {
            val letter = remainingLetters[i]
            currentRemainingLetters.add(letter)
        }
        hmBackend.setAvailableChars(currentRemainingLetters)
    }

    private fun showVowels() {
        val disabledVowels = hmBackend.getDisabledVowels()
        if (disabledVowels == false) {
            hmBackend.setDisabledVowels(true)
        }
        else {
            Log.d(TAG,"Already displayed all the vowels!")
        }
    }

    private fun isVowel(letter: Char): Boolean {
        return letter == 'A' || letter == 'E' || letter == 'I' || letter == 'O' || letter == 'U'
    }

}