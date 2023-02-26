package com.cs501.cs501app.assignment4.hangman

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.cs501.cs501app.databinding.FragmentChooseLetterBinding

private const val TAG = "ChooseLetterFragment"

class ChooseLetterFragment: Fragment() {
    private val backend by viewModels<HMBackend>()
    private var _binding: FragmentChooseLetterBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =
            FragmentChooseLetterBinding.inflate(layoutInflater, container, false)
        binding.hangmanHintContentLabel.text = backend.getHint()
        return binding.root
    }




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            hangmanBtnA.setOnClickListener {
                var text = hangmanBtnA.text.toString()
                val c: Char = text.first()
                backend.letterChecker(c)
                hangmanBtnA.isEnabled = false
            }
            hangmanBtnB.setOnClickListener {
                var text = hangmanBtnB.text.toString()
                val c: Char = text.first()
                backend.letterChecker(c)
                hangmanBtnB.isEnabled = false
            }
            hangmanBtnC.setOnClickListener {
                var text = hangmanBtnC.text.toString()
                val c: Char = text.first()
                backend.letterChecker(c)
                hangmanBtnC.isEnabled = false
            }
            hangmanBtnD.setOnClickListener {
                var text = hangmanBtnD.text.toString()
                val c: Char = text.first()
                backend.letterChecker(c)
                hangmanBtnD.isEnabled = false
            }
            hangmanBtnE.setOnClickListener {
                var text = hangmanBtnE.text.toString()
                val c: Char = text.first()
                backend.letterChecker(c)
                hangmanBtnE.isEnabled = false
            }
            hangmanBtnF.setOnClickListener {
                var text = hangmanBtnF.text.toString()
                val c: Char = text.first()
                backend.letterChecker(c)
                hangmanBtnF.isEnabled = false
            }
            hangmanBtnG.setOnClickListener {
                var text = hangmanBtnG.text.toString()
                val c: Char = text.first()
                backend.letterChecker(c)
                hangmanBtnG.isEnabled = false
            }
            hangmanBtnH.setOnClickListener {
                var text = hangmanBtnH.text.toString()
                val c: Char = text.first()
                backend.letterChecker(c)
                hangmanBtnH.isEnabled = false
            }
            hangmanBtnI.setOnClickListener {
                var text = hangmanBtnI.text.toString()
                val c: Char = text.first()
                backend.letterChecker(c)
                hangmanBtnI.isEnabled = false
            }
            hangmanBtnJ.setOnClickListener {
                var text = hangmanBtnJ.text.toString()
                val c: Char = text.first()
                backend.letterChecker(c)
                hangmanBtnJ.isEnabled = false
            }
            hangmanBtnK.setOnClickListener {
                var text = hangmanBtnK.text.toString()
                val c: Char = text.first()
                backend.letterChecker(c)
                hangmanBtnK.isEnabled = false
            }
            hangmanBtnL.setOnClickListener {
                var text = hangmanBtnL.text.toString()
                val c: Char = text.first()
                backend.letterChecker(c)
                hangmanBtnL.isEnabled = false
            }
            hangmanBtnM.setOnClickListener {
                var text = hangmanBtnM.text.toString()
                val c: Char = text.first()
                backend.letterChecker(c)
                hangmanBtnM.isEnabled = false
            }
            hangmanBtnN.setOnClickListener {
                var text = hangmanBtnN.text.toString()
                val c: Char = text.first()
                backend.letterChecker(c)
                hangmanBtnN.isEnabled = false
            }
            hangmanBtnO.setOnClickListener {
                var text = hangmanBtnO.text.toString()
                val c: Char = text.first()
                backend.letterChecker(c)
                hangmanBtnO.isEnabled = false
            }
            hangmanBtnP.setOnClickListener {
                var text = hangmanBtnP.text.toString()
                val c: Char = text.first()
                backend.letterChecker(c)
                hangmanBtnP.isEnabled = false
            }
            hangmanBtnQ.setOnClickListener {
                var text = hangmanBtnQ.text.toString()
                val c: Char = text.first()
                backend.letterChecker(c)
                hangmanBtnQ.isEnabled = false
            }
            hangmanBtnR.setOnClickListener {
                var text = hangmanBtnR.text.toString()
                val c: Char = text.first()
                backend.letterChecker(c)
                hangmanBtnR.isEnabled = false
            }
            hangmanBtnS.setOnClickListener {
                var text = hangmanBtnS.text.toString()
                val c: Char = text.first()
                backend.letterChecker(c)
                hangmanBtnS.isEnabled = false
            }
            hangmanBtnT.setOnClickListener {
                var text = hangmanBtnT.text.toString()
                val c: Char = text.first()
                backend.letterChecker(c)
                hangmanBtnT.isEnabled = false
            }
            hangmanBtnU.setOnClickListener {
                var text = hangmanBtnU.text.toString()
                val c: Char = text.first()
                backend.letterChecker(c)
                hangmanBtnU.isEnabled = false
            }
            hangmanBtnV.setOnClickListener {
                var text = hangmanBtnV.text.toString()
                val c: Char = text.first()
                backend.letterChecker(c)
                hangmanBtnV.isEnabled = false
            }
            hangmanBtnW.setOnClickListener {
                var text = hangmanBtnW.text.toString()
                val c: Char = text.first()
                backend.letterChecker(c)
                hangmanBtnW.isEnabled = false
            }

            hangmanBtnX.setOnClickListener {
                var text = hangmanBtnX.text.toString()
                val c: Char = text.first()
                backend.letterChecker(c)
                hangmanBtnX.isEnabled = false
            }
            hangmanBtnY.setOnClickListener {
                var text = hangmanBtnY.text.toString()
                val c: Char = text.first()
                backend.letterChecker(c)
                hangmanBtnY.isEnabled = false
            }
            hangmanBtnZ.setOnClickListener {
                var text = hangmanBtnZ.text.toString()
                val c: Char = text.first()
                backend.letterChecker(c)
                hangmanBtnZ.isEnabled = false
            }

        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}