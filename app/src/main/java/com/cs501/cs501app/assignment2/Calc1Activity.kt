package com.cs501.cs501app.assignment2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.cs501.cs501app.databinding.ActivityCalc1Binding
import com.cs501.cs501app.utils.Alert

class Calc1Activity : AppCompatActivity() {
    private lateinit var binding: ActivityCalc1Binding

    // chars showed on the UI
    var operators_char_array = arrayOf("➕", "➖", "✖", "️➗", "mod")

    // chars converted to calFunc expr
    var operators_expr_array = arrayOf("+", "-", "x", "/", "%")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalc1Binding.inflate(layoutInflater)
        setContentView(binding.root)

        ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            operators_char_array
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerOperator.adapter = adapter
        }

        binding.btnOp.setOnClickListener { view ->
            // read the values from the edit text fields
            var num1: Float = 0f
            var num2: Float = 0f
            try {
                num1 = binding.editOp1.text.toString().toFloat()
                num2 = binding.editOp2.text.toString().toFloat()
            } catch (e: Exception) {
                Alert.fail(view, "Invalid input")
                return@setOnClickListener
            }
            val operator = binding.spinnerOperator.selectedItem.toString()
            val expr: String = num1.toString() +
                    operators_expr_array[operators_char_array.indexOf(operator)] +
                    num2.toString()
            val resultStr: String = try {
                calFunc.evalExpr(expr)
            } catch (e: Exception) {
                "Error:$e"
            }
            binding.resultText.text = resultStr
            Alert.success(view, resultStr)
        }
    }
}