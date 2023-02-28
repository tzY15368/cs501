package com.cs501.cs501app.assignment4.hangman

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.cs501.cs501app.R
import kotlinx.android.synthetic.main.fragment_input.*

private const val TAG = "InputFragment"
//  define an interface in the InputFragment and make the parent activity implement it.

class InputFragment : Fragment() {
    private var _model: HMBackend? = null
    private val model
        get() = checkNotNull(_model) {
            "Cannot access model because it is null. Is the view visible?"
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _model = ViewModelProvider(requireActivity()).get(HMBackend::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_input, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hangman_input_text.text = model.currentDisplayWord.value
        model.currentDisplayWord.observe(viewLifecycleOwner, Observer {
            Log.d(TAG, "onViewCreated: $it")
            val displayWord = it
            hangman_input_text.text = displayWord
        })
    }

}