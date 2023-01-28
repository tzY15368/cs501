package com.bignerdranch.android.geoquiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    private lateinit var trueBtn: Button
    private lateinit var falseBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        trueBtn = findViewById(R.id.true_button)
        falseBtn = findViewById(R.id.false_button)
        trueBtn.setOnClickListener { view: View ->
            //...
            Snackbar.make(view, "hello", Snackbar.LENGTH_LONG).show()

        }

        falseBtn.setOnClickListener { view: View ->
            //...
        }
    }
}