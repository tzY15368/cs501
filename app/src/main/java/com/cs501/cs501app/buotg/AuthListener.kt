package com.cs501.cs501app.buotg

import com.cs501.cs501app.buotg.entities.User

interface AuthListener {

    fun onStarted()
    fun onSuccess(user: User)
    fun onFailure(message: String)
}