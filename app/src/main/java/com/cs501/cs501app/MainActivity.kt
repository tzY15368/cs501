package com.cs501.cs501app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.cs501.cs501app.R
import com.cs501.cs501app.assignment2.Calc1Activity
import com.cs501.cs501app.assignment2.Calc2Activity

class MainActivity : AppCompatActivity() {

    private lateinit var btn_2_1:Button
    private lateinit var btn_2_2:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn_2_1 = findViewById(R.id.button_2_1)
        btn_2_1.setOnClickListener{view->
            val intent = Intent(this, Calc1Activity::class.java)
            startActivity(intent)
        }
        btn_2_2 = findViewById(R.id.button_2_2)
        btn_2_2.setOnClickListener{view->
            val intent = Intent(this, Calc2Activity::class.java)
            startActivity(intent)
        }
    }
}