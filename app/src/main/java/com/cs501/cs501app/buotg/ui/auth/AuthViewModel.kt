package com.cs501.cs501app.buotg.ui.auth
import android.text.TextUtils
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cs501.cs501app.buotg.AuthListener
import com.cs501.cs501app.buotg.database.AppRepository
import com.cs501.cs501app.buotg.database.repositories.UserRepository
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repository: UserRepository
) : ViewModel() {
    var name : String? = null
    var email: String? = null
    var password: String? = null
    var passwordconfirm: String? = null

    var authListener: AuthListener? = null

    fun getLoggedInUser() = repository.getUser()

    fun onLoginButtonClick(view: View) {
        authListener?.onStarted()
        if (email.isNullOrEmpty() || password.isNullOrEmpty()) {
            authListener?.onFailure("Invalid email or password")
            return
        }
        //TODO: Coroutines


    }

    fun onLogin(view: View){
        Intent(view.context, LoginActivity::class.java).also {
            view.context.startActivity(it)
        }
    }

    fun onSignup(view: View){
        Intent(view.context, SignUpActivity::class.java).also {
            view.context.startActivity(it)
        }
    }

    fun onSignupButtonClick(view: View) {
        authListener?.onStarted()

        if (name.isNullOrEmpty()){
            authListener?.onFailure("Name is required")
            return
        }

        if (email.isNullOrEmpty()) {
            authListener?.onFailure("Email is required")
            return
        }

        if (password.isNullOrEmpty()){
            authListener?.onFailure("Please enter a password")
            return
        }

        if (password != passwordconfirm){
            authListener?.onFailure("Password did not match")
            return
        }
        //TODO: Coroutines


    }

}
