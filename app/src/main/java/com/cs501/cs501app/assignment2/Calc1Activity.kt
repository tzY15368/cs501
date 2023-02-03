package com.cs501.cs501app.assignment2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.cs501.cs501app.R
import com.cs501.cs501app.utils.Alert

class Calc1Activity : AppCompatActivity() {
    private lateinit var op_1: TextView
    private lateinit var op_2: TextView
    private lateinit var op_operand: TextView
    private lateinit var btn_Op: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calc1)
        op_1 = findViewById(R.id.edit_op1)
        op_2 = findViewById(R.id.edit_op2)
        op_operand = findViewById(R.id.editOperand)
        btn_Op = findViewById(R.id.btn_op)
        // add hooks
        btn_Op.setOnClickListener { view ->
            Alert.success(view, "hello")
        }
    }
}