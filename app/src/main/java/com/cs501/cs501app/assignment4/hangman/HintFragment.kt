package com.cs501.cs501app.assignment4.hangman

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cs501.cs501app.R
import com.cs501.cs501app.databinding.FragmentHintBinding

private const val TAG = "HintFragment"

interface HintFragmentListener {
    fun onHintPressed()
}

class HintFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var listener: HintFragmentListener
//    private lateinit var binding: FragmentHintBinding

    private var _binding: FragmentHintBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }
    private var hintCounter = 0
    private var disabledLetters = mutableListOf<Char>()
    private var disabledVowels = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is HintFragmentListener) {
            listener = context
            Log.d(TAG, "onAttach: ")
            listener.onHintPressed()
        } else {
            throw RuntimeException("$context must implement HintFragmentListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHintBinding.inflate(inflater, container, false)
        binding.hintButton.setOnClickListener(View.OnClickListener {
                if (hintCounter == 0) {
                    Log.d(TAG, "This is your first hint!")
                } else if (hintCounter == 1) {
                    disableHalfLetters()
                    Log.d(TAG, "Half of the letters have been disabled.")
                } else if (hintCounter == 2) {
                    showVowels()
                    Log.d(TAG, "All the vowels have been shown.")
                } else {
                    Log.d(TAG, "Hint not available.")
                }
                hintCounter++
            }
        )
//        binding.apply{
//            // ...
//        }
        listener.onHintPressed()
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
        binding.hintButton.text = text
    }

    private fun disableHalfLetters() {
        val remainingLetters = getRemainingLetters()
        val half = remainingLetters.size / 2
        for (i in 0 until half) {
            val letter = remainingLetters[i]
            disableLetterButton(letter)
            disabledLetters.add(letter)
        }
    }

    private fun showVowels() {
        val remainingLetters = getRemainingLetters()
        for (letter in remainingLetters) {
            if (isVowel(letter)) {
                showLetterButton(letter)
            }
        }
        disabledVowels = true
    }

    private fun getRemainingLetters(): List<Char> {
        val letters = mutableListOf<Char>()
        for (i in 0 until letterButtonsContainer.childCount) {
            val button = letterButtonsContainer.getChildAt(i) as Button
            val letter = button.text.toString()[0]
            if (!disabledLetters.contains(letter) && (disabledVowels || !isVowel(letter))) {
                letters.add(letter)
            }
        }
        return letters
    }

    private fun disableLetterButton(letter: Char) {
        for (i in 0 until letterButtonsContainer.childCount) {
            val button = letterButtonsContainer.getChildAt(i) as Button
            if (button.text.toString()[0] == letter) {
                button.isEnabled = false
            }
        }
    }

    private fun showLetterButton(letter: Char) {
        for (i in 0 until letterButtonsContainer.childCount) {
            val button = letterButtonsContainer.getChildAt(i) as Button
            if (button.text.toString()[0] == letter) {
                button.visibility = View.VISIBLE
            }
        }
    }

    private fun isVowel(letter: Char): Boolean {
        return letter == 'A' || letter == 'E' || letter == 'I' || letter == 'O' || letter == 'U'
    }
}