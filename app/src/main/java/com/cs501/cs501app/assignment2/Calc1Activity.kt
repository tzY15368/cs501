package com.cs501.cs501app.assignment2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import com.cs501.cs501app.R
import com.cs501.cs501app.databinding.ActivityCalc1Binding
import com.cs501.cs501app.utils.Alert

class Calc1Activity : AppCompatActivity() {
    private lateinit var binding: ActivityCalc1Binding

    fun calculate(a:String, b:String , operand:String):String{
        print(a)
        val first = a.toInt()
        val second = b.toInt()
        if (operand == "+"){
            return (first + second).toString()
        }else if (operand == "-"){
            return (first - second).toString()
        }else if (operand == "*"){
            return (first * second).toString()
        }else if (operand == "/"){
            if (second == 0){
                return "Error!"
            }
            return (first / second).toString()
        }else if (operand == "%"){
            if (second == 0){
                return "Error!"
            }
            return (first % second).toString()
        }
        return "Not a valid calculation"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalc1Binding.inflate(layoutInflater)
        setContentView(binding.root)
        var num_1 = findViewById<EditText>(R.id.edit_op1).text
        var num_2 = findViewById<EditText>(R.id.edit_op2).text
        var operand = findViewById<EditText>(R.id.editOperand).text

        binding.btnOp.setOnClickListener { view ->
            Alert.success(view, calculate(num_1.toString(),num_2.toString(),operand.toString()))
        }
    }
}