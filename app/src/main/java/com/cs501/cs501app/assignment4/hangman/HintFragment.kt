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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.cs501.cs501app.R
import com.cs501.cs501app.assignment3.flashcard.FCBackend
import com.cs501.cs501app.databinding.FragmentHintBinding
import kotlin.system.exitProcess

private const val TAG = "HintFragment"

class HintFragment : Fragment(){

    private var _model: HMBackend? = null
    private val model
        get() = checkNotNull(_model) {
            "Cannot access model because it is null. Is the view visible?"
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _model = ViewModelProvider(requireActivity()).get(HMBackend::class.java)
    }
    private var _binding: FragmentHintBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(TAG, "onAttach: ")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHintBinding.inflate(inflater, container, false)
        binding.hintButton.setOnClickListener(View.OnClickListener {
            model.getHint()
            }
        )
        binding.resetButton.setOnClickListener(View.OnClickListener {
            model.reset()
        })
        model.hint.observe(viewLifecycleOwner, Observer{ hint ->
            binding.hintText.text = hint
        })
        model.gameState.observe(viewLifecycleOwner, Observer{ state ->
                binding.hintButton.isEnabled = state==GameState.IN_PROGRESS
        })
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

}