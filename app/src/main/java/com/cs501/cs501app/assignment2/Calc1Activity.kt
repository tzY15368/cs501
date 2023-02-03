package com.cs501.cs501app.assignment2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.cs501.cs501app.R
import com.cs501.cs501app.databinding.ActivityCalc1Binding
import com.cs501.cs501app.utils.Alert

class Calc1Activity : AppCompatActivity() {
    private lateinit var binding: ActivityCalc1Binding
    var operators_array= arrayOf("➕","➖","✖","️➗","mod")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalc1Binding.inflate(layoutInflater)
        setContentView(binding.root)

        ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            operators_array
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerOperator.adapter = adapter
        }

        binding.btnOp.setOnClickListener { view ->
            Alert.success(view, "hello")
        }
    }
}