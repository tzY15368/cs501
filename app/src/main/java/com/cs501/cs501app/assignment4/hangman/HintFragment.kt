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

public interface HintFragmentListener {
    fun onHintPressed()
}

class HintFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var listener: HintFragmentListener
    private lateinit var binding: FragmentHintBinding

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
        binding = FragmentHintBinding.inflate(inflater, container, false)
        binding.apply{
            // ...
        }
        return binding.root
    }
}