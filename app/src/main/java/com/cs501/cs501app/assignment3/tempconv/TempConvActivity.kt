package com.cs501.cs501app.assignment3.tempconv

import android.os.Bundle
import android.util.Log
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.appcompat.app.AppCompatActivity
import com.cs501.cs501app.databinding.ActivityTempConvBinding

class TempConvActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTempConvBinding;
    private var tempC = 0
    private var tempF = 32
    private var strChangeThres = 20
    private val STR_WARMER = "I wish it was warmer."
    private val STR_COLDER = "I wish it was colder."

    private fun updateText(temp: Int, isTempC: Boolean) {
        Log.d("TempConvActivity", "updateText: $temp, $isTempC")
        if (isTempC) {
            tempC = temp
            tempF = ((tempC * 1.8 + 32).toInt())
        } else {
            tempF = temp
            tempC = (((tempF - 32) * .5556).toInt())
        }
        Log.d("tempC", tempC.toString())
        binding.seekBarC.progress = tempC
        binding.seekBarF.progress = tempF
        binding.textViewTempC.text = tempC.toString()
        binding.textViewTempF.text = tempF.toString()
        if (tempC < strChangeThres) {
            binding.textViewIntMsg.text = STR_WARMER
        } else {
            binding.textViewIntMsg.text = STR_COLDER
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTempConvBinding.inflate(layoutInflater)
        binding.seekBarC.incrementProgressBy(1)
        binding.seekBarF.incrementProgressBy(1)
        setContentView(binding.root)
        updateText(0, true)
        class MyOnseekBarChangeListener(// constructor with one argument of type int
            private var tempLowerThres: Int, private var isTempC: Boolean
        ) : OnSeekBarChangeListener {
            private var hasInput = false
            override fun onProgressChanged(seekBar: SeekBar, tempC: Int, able: Boolean) {
                if (!hasInput) return
                if (tempC < tempLowerThres) {
                    seekBar.progress = tempLowerThres
                    return
                }
                updateText(tempC, isTempC)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
                hasInput = true
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                hasInput = false
            }
        }
        binding.seekBarC.setOnSeekBarChangeListener(MyOnseekBarChangeListener(0, true))
        binding.seekBarF.setOnSeekBarChangeListener(MyOnseekBarChangeListener(32, false))
    }
}

