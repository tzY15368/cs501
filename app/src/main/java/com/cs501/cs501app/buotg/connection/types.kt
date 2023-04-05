package com.cs501.cs501app.buotg.connection

import com.cs501.cs501app.buotg.entities.User

open class StdResponse{
    lateinit var message: String
}

class LoginResponse: StdResponse(){
    lateinit var token: String
    lateinit var user: User
}

class SignupResponse: StdResponse(){
    // echoes back the user so we can save it
    lateinit var user: User
}
