package com.cs501.cs501app.assignment4.boggle

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.cs501.cs501app.R
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.ViewModelProvider


class BoggleActivity : AppCompatActivity() {
    private lateinit var viewModel: FragmentSharedViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_boggle)
        viewModel = ViewModelProvider(this).get(FragmentSharedViewModel::class.java)
    }
}