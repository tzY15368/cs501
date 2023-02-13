package com.cs501.cs501app.assignment3.flashcard

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import com.cs501.cs501app.R
import com.cs501.cs501app.databinding.ActivityFcloginBinding
import com.cs501.cs501app.utils.Alert
import com.cs501.cs501app.utils.TAlert


class FCLoginActivity : AppCompatActivity(), View.OnClickListener{
    private lateinit var binding: ActivityFcloginBinding
    companion object {
        const val TAG = "FCLoginActivity"
        const val VALID_NAME = "admin"
        const val VALID_PSD = "123456"

    }

    private var nameLegal: Boolean = true
    private var psdLegal: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFcloginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initEditText()
        initClickListener()
    }


    private fun initEditText() {
        binding.etLoginName.addTextChangedListener(nameWatcher)
        binding.etLoginPsd.addTextChangedListener(psdWatcher)
    }

    private fun initClickListener() {
        binding.etLoginName.setOnClickListener(this)
        binding.etLoginPsd.setOnClickListener(this)
        binding.btGoLogin.setOnClickListener(this)
    }


    private val nameWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable) {
            val input = s.toString()
            Log.v(TAG, "nameWatcher_input=$input")
            nameLegal = input == VALID_NAME
            Log.v(TAG, "nameWatcher_nameLegal=$nameLegal")
        }
    }

    private val psdWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable) {
            val input = s.toString()
            Log.v(TAG, "psdWatcher_input=$input")
            psdLegal = input == VALID_PSD
            Log.v(TAG, "psdWatcher_psdLegal=$psdLegal")
        }
    }


    override fun onClick(v: View?) {
        if (v == null) {
            return
        }
        when (v.id) {
            R.id.btGoLogin -> {
                if (!nameLegal) {
                    Alert.fail(v, "name invalid! Please try again.")
                    return
                }
                if (!psdLegal) {
                    Alert.fail(v, "password invalid! Please try again.")
                    return
                }
                Alert.success(v, "Login successfully! $VALID_NAME")
                //跳转到home
                val intent: Intent = Intent(this, FCHome::class.java)

                startActivity(intent)
                TAlert.success(applicationContext, "Welcome admin !")
            }
        }
    }


}