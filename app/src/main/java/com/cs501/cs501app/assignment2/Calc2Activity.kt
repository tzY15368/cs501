package com.cs501.cs501app.assignment2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Log
import com.cs501.cs501app.databinding.ActivityCalc2Binding
import com.cs501.cs501app.utils.Alert

class Calc2Activity : AppCompatActivity() {
    private lateinit var binding: ActivityCalc2Binding
    // operateCount should be less than digitCount to make sure expression valid

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityCalc2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        var str = "" //binding.msg.text.toString();

        binding.editAdvanced?.inputType = InputType.TYPE_NULL

        binding.btn0.setOnClickListener {
            updateSettings("0")
        }

        binding.btn1.setOnClickListener {
            updateSettings("1")
        }
        binding.btn2.setOnClickListener {
            updateSettings("2")
        }
        binding.btn3.setOnClickListener {
            updateSettings("3")
        }
        binding.btn4.setOnClickListener {
            updateSettings("4")
        }
        binding.btn5.setOnClickListener {
            updateSettings("5")
        }
        binding.btn6.setOnClickListener {
            updateSettings("6")
        }
        binding.btn7.setOnClickListener {
            updateSettings("7")
        }
        binding.btn8.setOnClickListener {
            updateSettings("8")
        }
        binding.btn9.setOnClickListener {
            updateSettings("9")
        }

        binding.btnDiv.setOnClickListener {
            updateSettings("/")
        }

        binding.btnMulti.setOnClickListener {
            updateSettings("*")
        }
        binding.btnPlus.setOnClickListener {
            updateSettings("+")
        }
        binding.btnSub.setOnClickListener {
            updateSettings("-")
        }
        binding.btnSqrt.setOnClickListener {
            updateSettings("√")
        }

        binding.btnPoint.setOnClickListener {
            updateSettings(".")
        }

        binding.btnMod.setOnClickListener{
            updateSettings("%")
        }
        binding.btnDel.setOnClickListener{
            str = binding.editAdvanced.text.toString()
            str = str.substring(0, str.length - 1)
            binding.editAdvanced.setText(str)
            binding.editAdvanced.setSelection(binding.editAdvanced.length())
        }
        binding.btnAc.setOnClickListener{
            str = ""
            binding.editAdvanced.setText(str)
            binding.editAdvanced.setSelection(binding.editAdvanced.length())
        }

        binding.btnEquals.setOnClickListener { view ->
            str = binding.editAdvanced.text.toString()
            try {
                val resultMsg = calFunc.evalExpr(str)
                Alert.success(view, resultMsg)
                binding.editAdvanced.setText(resultMsg)
            } catch (e: Exception) {

                val errorMsg = "ERROR:${e.message}"
                Alert.fail(view, errorMsg)
                Log.e("Calc2", errorMsg)
            }
        }
    }

    private fun updateSettings(add_str: String) {
        var temp = binding.editAdvanced.text.toString()
        temp += add_str
//        binding.msg.text = temp
        binding.editAdvanced.setText(temp)
        binding.editAdvanced.setSelection(binding.editAdvanced.length())
    }

}