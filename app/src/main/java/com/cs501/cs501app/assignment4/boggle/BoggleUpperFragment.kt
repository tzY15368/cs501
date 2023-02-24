package com.cs501.cs501app.assignment4.boggle

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cs501.cs501app.databinding.FragmentBoggleUpperBinding
import java.util.*

class BoggleUpperFragment : Fragment(){
//    private lateinit var crime: Crime
    private var _binding: FragmentBoggleUpperBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        crime = Crime(
//            id = UUID.randomUUID(),
//            title = "",
//            date = Date(),
//            isSolved = false
//        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =
            FragmentBoggleUpperBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            button00.setOnClickListener {
                Log.d("test", "test")
            }
//            crimeTitle.doOnTextChanged { text, _, _, _ ->
//                crime = crime.copy(title = text.toString())
//            }
//            crimeDate.apply {
//                text = crime.date.toString()
//                isEnabled = false
//            }
//
//            crimeSolved.setOnCheckedChangeListener { _, isChecked ->
//                crime = crime.copy(isSolved = isChecked)
//            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}