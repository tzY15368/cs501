package com.cs501.cs501app.assignment3.tempconv

import android.os.Bundle
import android.util.Log
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.appcompat.app.AppCompatActivity
import com.cs501.cs501app.databinding.ActivityTempConvBinding
class TempConvActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTempConvBinding;
    private var tempC = 0.0
    private var tempF = 0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTempConvBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.seekBarC.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, tempC: Int, able: Boolean) {
                tempF = (Integer.valueOf(tempC.toString()) * 1.8 + 32)
                binding.textViewTempC.text = tempC.toString()
                binding.textViewTempF.text = tempF.toString()
                //Log.e("temp",tempC.toString())
                if (tempC < 20) {
                    binding.textViewIntMsg.text = "I wish it were warmer."
                } else {
                    binding.textViewIntMsg.text = "I wish it were colder."
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {
                binding.seekBarF.progress = tempF.toInt()
            }
        })
        binding.seekBarF.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, tempF: Int, able: Boolean) {
                tempC = ((Integer.valueOf(tempF.toString()) - 32) * .5556)
                binding.textViewTempF.text = tempF.toString()
                binding.textViewTempC.text = tempC.toString()
                if (tempC < 20) {
                    binding.textViewIntMsg.text = "I wish it were warmer."
                } else {
                    binding.textViewIntMsg.text = "I wish it were colder."
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {
                binding.seekBarC.progress = tempC.toInt()
                if (tempF < 20) {
                    binding.textViewIntMsg.text = "I wish it were warmer."
                } else {
                    binding.textViewIntMsg.text = "I wish it were colder."
                }
            }
        })
    }
}