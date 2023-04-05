package com.cs501.cs501app.buotg.user_setup.auth
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.cs501.cs501app.R
import com.cs501.cs501app.buotg.AuthListener
import com.cs501.cs501app.buotg.HomeActivity
import com.cs501.cs501app.buotg.entities.User
import com.cs501.cs501app.databinding.ActivityBuotgLoginBinding


//class LoginActivity : AppCompatActivity(), AuthListener{
//
//    private lateinit var viewModel: AuthViewModel
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        val binding: ViewDataBinding? =
//            DataBindingUtil.setContentView(this, R.layout.activity_buotg_login)
//
//        val viewModel = ViewModelProvider(this).get(AuthViewModel::class.java)
//
////        binding.viewmodel = viewModel
//
//        viewModel.authListener = this
//
//        viewModel.getLoggedInUser().observe(this, Observer { user ->
//            if (user != null){
//                Intent(this, HomeActivity::class.java).also {
//                    it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                    startActivity(it)
//                }
//            }
//        })
//    }
//
//    override fun onStarted() {
//        TODO("Not yet implemented")
//    }
//
//    override fun onSuccess(user: User) {
//        TODO("Not yet implemented")
//    }
//
//    override fun onFailure(message: String) {
//        TODO("Not yet implemented")
//    }
//
//}