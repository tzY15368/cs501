package com.cs501.cs501app.assignment4.hangman

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.compose.ui.text.toLowerCase
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.cs501.cs501app.databinding.FragmentChooseLetterBinding

private const val TAG = "ChooseLetterFragment"

class ChooseLetterFragment: Fragment() {
    private var _model: HMBackend? = null
    private val model
        get() = checkNotNull(_model) {
            "Cannot access model because it is null. Is the view visible?"
        }
    private var _binding: FragmentChooseLetterBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _model = ViewModelProvider(requireActivity()).get(HMBackend::class.java)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =
            FragmentChooseLetterBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    private fun handleInput(btn: Button) {
        model.handleInput(btn.text.first())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // add hangmanBtn A-Z to a list
        val btnList = listOf(
            binding.hangmanBtnA,
            binding.hangmanBtnB,
            binding.hangmanBtnC,
            binding.hangmanBtnD,
            binding.hangmanBtnE,
            binding.hangmanBtnF,
            binding.hangmanBtnG,
            binding.hangmanBtnH,
            binding.hangmanBtnI,
            binding.hangmanBtnJ,
            binding.hangmanBtnK,
            binding.hangmanBtnL,
            binding.hangmanBtnM,
            binding.hangmanBtnN,
            binding.hangmanBtnO,
            binding.hangmanBtnP,
            binding.hangmanBtnQ,
            binding.hangmanBtnR,
            binding.hangmanBtnS,
            binding.hangmanBtnT,
            binding.hangmanBtnU,
            binding.hangmanBtnV,
            binding.hangmanBtnW,
            binding.hangmanBtnX,
            binding.hangmanBtnY,
            binding.hangmanBtnZ
        )
        binding.apply {
            // add click listener to each button
            // when click, call handleInput()
            for (btn in btnList) {
                btn.setOnClickListener {
                    handleInput(btn)
                }
            }
        }
        model.remainingLetters.observe(viewLifecycleOwner) {
            // turn everything in it to upper case
            val that = it.map { it.uppercaseChar() }
            binding.apply {
                for(btn in btnList) {
                    btn.isEnabled = btn.text.first() in that
                }
            }
        }
        model.gameState.observe(viewLifecycleOwner) {
            binding.apply {
                for(btn in btnList) {
                    btn.isEnabled = it == GameState.IN_PROGRESS
                }
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}