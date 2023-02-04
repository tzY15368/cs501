package com.cs501.cs501app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.cs501.cs501app.assignment2.Calc1Activity
import com.cs501.cs501app.assignment2.Calc2Activity
import com.cs501.cs501app.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.button21.setOnClickListener {
            val intent = Intent(this, Calc1Activity::class.java)
            startActivity(intent)
        }
        binding.button22.setOnClickListener {
            val intent = Intent(this, Calc2Activity::class.java)
            startActivity(intent)
        }
    }
}