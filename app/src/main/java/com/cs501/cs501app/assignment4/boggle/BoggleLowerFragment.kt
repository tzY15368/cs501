package com.cs501.cs501app.assignment4.boggle

import android.os.Bundle
import androidx.lifecycle.Observer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.cs501.cs501app.databinding.FragmentBoggleLowerBinding
import com.cs501.cs501app.utils.Alert
import com.cs501.cs501app.utils.TAlert
import java.util.*

class BoggleLowerFragment : Fragment() {

    private var _binding: FragmentBoggleLowerBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }

    private var _model: FragmentSharedViewModel? = null
    private val model
        get() = checkNotNull(_model) {
            "Cannot access model because it is null. Is the view visible?"
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _model = ViewModelProvider(requireActivity()).get(FragmentSharedViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =
            FragmentBoggleLowerBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            boggleNewGame.setOnClickListener {
                model.setMessageNewGame("lower fragment clicks NEW GAME button")
            }
        }

        model.getMessageClear().observe(viewLifecycleOwner, Observer { message ->
            // Update UI with the new message
            Alert.success(view, message)
        })
        model.getMessageSubmit().observe(viewLifecycleOwner, Observer { message ->
            // Update UI with the new message
            Alert.success(view, message)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}