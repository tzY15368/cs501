package com.cs501.cs501app.assignment2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.cs501.cs501app.R
import com.cs501.cs501app.assignment2.calFunc.cal
import com.cs501.cs501app.databinding.ActivityCalc1Binding
import com.cs501.cs501app.databinding.ActivityCalc2Binding

class Calc2Activity : AppCompatActivity(){
    private lateinit var binding: ActivityCalc2Binding
    // operateCount should be less than digitCount to make sure expression valid


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityCalc2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        var str = binding.msg.text.toString();

        binding.btn0.setOnClickListener{
            str += "0"
            binding.msg.text = str
        }

        binding.btn1.setOnClickListener{
            str += "1"
            binding.msg.text = str
        }
        binding.btn2.setOnClickListener{
            str += "2"
            binding.msg.text = str
        }
        binding.btn3.setOnClickListener{
            str += "3"
            binding.msg.text = str
        }
        binding.btn4.setOnClickListener{
            str += "4"
            binding.msg.text = str
        }
        binding.btn5.setOnClickListener{
            str += "5"
            binding.msg.text = str
        }
        binding.btn6.setOnClickListener{
            str += "6"
            binding.msg.text = str
        }
        binding.btn7.setOnClickListener{
            str += "7"
            binding.msg.text = str
        }
        binding.btn8.setOnClickListener{
            str += "8"
            binding.msg.text = str
        }
        binding.btn9.setOnClickListener{
            str += "9"
            binding.msg.text = str
        }

        binding.btnDiv.setOnClickListener{
            str += "/"
            binding.msg.text = str
        }

        binding.btnMulti.setOnClickListener{
            str += "*"
            binding.msg.text = str
        }
        binding.btnPlus.setOnClickListener{
            str += "+"
            binding.msg.text = str
        }
        binding.btnSub.setOnClickListener{
            str += "-"
            binding.msg.text = str
        }
        binding.btnSqrt.setOnClickListener{
            str += "√"
            binding.msg.text = str
        }

        binding.btnPoint.setOnClickListener{
            str += "."
            binding.msg.text = str
        }

        binding.btnEquals.setOnClickListener{
            binding.msg.text = calFunc.calc(cal(str))//输入结束，转为逆波兰表达式
        }
    }

}