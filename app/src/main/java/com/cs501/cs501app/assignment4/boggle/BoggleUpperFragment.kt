package com.cs501.cs501app.assignment4.boggle

import android.content.Context
import android.content.res.AssetManager
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.fragment.app.activityViewModels
import com.cs501.cs501app.R
import com.cs501.cs501app.databinding.FragmentBoggleUpperBinding
import com.cs501.cs501app.utils.Alert
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.io.InputStreamReader
import java.util.*
import kotlin.math.absoluteValue

class BoggleUpperFragment: Fragment() {

    private var _binding: FragmentBoggleUpperBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }

    private var _model: FragmentSharedViewModel? = null
    private val model
        get() = checkNotNull(_model) {
            "Cannot access model because it is null. Is the view visible?"
        }

    private var _am: AssetManager? = null
    private val am
        get() = checkNotNull(_am) {
            "Cannot access am because it is null. Is the view visible?"
        }

    private var _inStream: InputStream? = null
    private val inStream
        get() = checkNotNull(_inStream) {
            "Cannot access fileDescriptor because it is null. Is the view visible?"
        }

    private var _boggleBackend: BoggleGame? = null
    private val boggleBackend
        get() = checkNotNull(_boggleBackend) {
            "Cannot access boggleBackend because it is null. Is the view visible?"
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _model = ViewModelProvider(requireActivity()).get(FragmentSharedViewModel::class.java)
        _am = requireContext().assets
        _inStream = am.open("words.txt")
        _boggleBackend = BoggleGame(4, 4, inStream, requireContext())
        boggleBackend.displayBoard()
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
        getButtonsChar()
        binding.apply {
            val buttonList = listOf(button00, button01, button02, button03,
                button10, button11, button12, button13,
                button20, button21, button22, button23,
                button30, button31, button32, button33)

            var curButtonList = listOf<Button>()
            var prevButton: View? = null

            boggleClear.setOnClickListener {
                resultView.text = ""
                boggleBackend.clearAnswer()
                resetButtonState(curButtonList)
                prevButton = null
                curButtonList = listOf()
                model.setMessageClear("upper fragment clicks CLEAR button")
            }

            boggleSubmit.setOnClickListener {
                if (boggleBackend.checkAnswer()) {
                    curButtonList = listOf()
                }
                Log.d("checkAnswer", boggleBackend.answer)
                Log.d("checkScore", boggleBackend.getScore().toString())
                model.setMessageSubmit(boggleBackend.getScore().toString())
            }

            buttonList.forEach { button ->
                button.setOnClickListener {
                    if (prevButton == null || isNearButton(button, prevButton)) {
                        button.isEnabled = false
                        curButtonList += button
                        boggleBackend.addAnswer(button.text[0])
                        updateSelectedWords(resultView.text.toString() + button.text.toString())
                        prevButton = button
                    } else {
                        Alert.fail(view, "Illegal position")
                    }
                }
            }

        }

        model.getMessageNewGame().observe(viewLifecycleOwner, Observer { message ->
            // Update UI with the new message
            boggleBackend.generateBoard()
            binding.apply {
                boggleClear.performClick()
            }
            getButtonsChar()
            boggleBackend.setScore(0)
            Alert.success(view, message)
        })
    }

    private fun getButtonsChar() {
        binding.apply {
            button00.text = boggleBackend.getBoardByPosition(0, 0).toString()
            button01.text = boggleBackend.getBoardByPosition(0, 1).toString()
            button02.text = boggleBackend.getBoardByPosition(0, 2).toString()
            button03.text = boggleBackend.getBoardByPosition(0, 3).toString()

            button10.text = boggleBackend.getBoardByPosition(1, 0).toString()
            button11.text = boggleBackend.getBoardByPosition(1, 1).toString()
            button12.text = boggleBackend.getBoardByPosition(1, 2).toString()
            button13.text = boggleBackend.getBoardByPosition(1, 3).toString()

            button20.text = boggleBackend.getBoardByPosition(2, 0).toString()
            button21.text = boggleBackend.getBoardByPosition(2, 1).toString()
            button22.text = boggleBackend.getBoardByPosition(2, 2).toString()
            button23.text = boggleBackend.getBoardByPosition(2, 3).toString()

            button30.text = boggleBackend.getBoardByPosition(3, 0).toString()
            button31.text = boggleBackend.getBoardByPosition(3, 1).toString()
            button32.text = boggleBackend.getBoardByPosition(3, 2).toString()
            button33.text = boggleBackend.getBoardByPosition(3, 3).toString()
        }
    }

    private fun updateSelectedWords(str : String) {
        binding.apply {
            resultView.text = str;
        }
    }

    private fun resetButtonState(curButtonList : List<Button>) {
        binding.apply {
            curButtonList.forEach { button ->
                button.isEnabled = true
            }
        }
    }

    private fun isNearButton(currButton: View, prevButton: View?): Boolean {
        if (prevButton == null) {
            return false
        }

        val currX = getXPosition(currButton)
        val currY = getYPosition(currButton)
        val prevX = getXPosition(prevButton)
        val prevY = getYPosition(prevButton)

        // Check if currButton and prevButton are adjacent horizontally, vertically, or diagonally.
        return (currX - prevX).absoluteValue <= 1 && (currY - prevY).absoluteValue <= 1
    }

    private fun getXPosition(button: View): Int {
        return when (button) {
            binding.button00, binding.button10, binding.button20, binding.button30 -> 0
            binding.button01, binding.button11, binding.button21, binding.button31 -> 1
            binding.button02, binding.button12, binding.button22, binding.button32 -> 2
            binding.button03, binding.button13, binding.button23, binding.button33 -> 3
            else -> -1
        }
    }

    private fun getYPosition(button: View): Int {
        return when (button) {
            binding.button00, binding.button01, binding.button02, binding.button03 -> 0
            binding.button10, binding.button11, binding.button12, binding.button13 -> 1
            binding.button20, binding.button21, binding.button22, binding.button23 -> 2
            binding.button30, binding.button31, binding.button32, binding.button33 -> 3
            else -> -1
        }
    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}