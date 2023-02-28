package com.cs501.cs501app.assignment4.hangman

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import com.cs501.cs501app.R
import kotlinx.android.synthetic.main.fragment_image.*

const val HP_PARAM = "CURRENT_HP"
private const val TAG = "ImageFragment"

class ImageFragment : Fragment() {
    private val backend by viewModels<HMBackend>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        arguments?.let {
//            val hp = it.getInt(HP_PARAM)
//
//            Log.d(TAG, "onCreate: $hp hp")
//        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_image, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val hp = backend.getHP()
        Log.d(TAG, "what: $hp hp" )
        if (hp == 0){
            hangmen.setImageResource(R.drawable.step7)
        }
        if (hp == 1){
            hangmen.setImageResource(R.drawable.step6)
        }
        if (hp == 2){
            hangmen.setImageResource(R.drawable.step5)
        }
        if (hp == 3){
            hangmen.setImageResource(R.drawable.step4)
        }
        if (hp == 4){
            hangmen.setImageResource(R.drawable.step3)
        }
        if (hp == 5){
            hangmen.setImageResource(R.drawable.step2)
        }
        if (hp == 6){
            hangmen.setImageResource(R.drawable.step1)
        }
    }
}