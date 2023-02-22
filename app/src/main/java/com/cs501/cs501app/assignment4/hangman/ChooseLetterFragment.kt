package com.cs501.cs501app.assignment4.hangman

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cs501.cs501app.databinding.ActivityGeoQuizBinding
import com.cs501.cs501app.databinding.FragmentChooseLetterBinding

private const val TAG = "ChooseLetterFragment"

class ChooseLetterFragment: Fragment() {

    private lateinit var binding: FragmentChooseLetterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.hangmanBtnA.setOnClickListener {
            binding.hangmanBtnA.isClickable = false
        }

    }
}