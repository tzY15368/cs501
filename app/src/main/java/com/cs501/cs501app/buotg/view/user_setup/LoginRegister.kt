package com.cs501.cs501app.buotg.view.user_setup

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cs501.cs501app.buotg.CustomButton
import com.cs501.cs501app.buotg.CustomText
import com.cs501.cs501app.buotg.CustomTextField
import com.cs501.cs501app.buotg.connection.LoginResponse
import com.cs501.cs501app.buotg.database.repositories.AppRepository
import com.cs501.cs501app.buotg.connection.StdResponse
import com.cs501.cs501app.buotg.database.entities.UserType
import com.cs501.cs501app.buotg.view.thirdParty.chatRoom.ChatApplication
import com.cs501.cs501app.utils.TAlert
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
//@Preview
@Composable
fun LoginRegister(done: () -> Unit = {}, msg: String = "", targetApp: ChatApplication) {
    val ctx = LocalContext.current
    val userRepo = AppRepository.get().userRepo()
    val coroutineScope = rememberCoroutineScope()
    val fullName = rememberSaveable { mutableStateOf("") }
    val email = rememberSaveable { mutableStateOf("") }
    val password = rememberSaveable { mutableStateOf("") }
    val isLogin = rememberSaveable { mutableStateOf(true) }

    LaunchedEffect(true) {
        coroutineScope.launch {
//            val user = userRepo.getCurrentUser()
//            if (user != null) {
//                Log.d("LoginRegister", "logging out user: $user")
//                AppRepository.get().userRepo().logout()
//            }
        }
    }

    val handleSubmit: () -> Unit = {
        coroutineScope.launch {
            if (email.value.isEmpty() || password.value.isEmpty()) {
                TAlert.fail(ctx, "Please enter your email and password")
                return@launch
            }
            val res: StdResponse? =
                if (isLogin.value) {
                    userRepo.userLogin(
                        ctx,
                        email.value,
                        password.value
                    )
                } else {
                    userRepo.userSignup(
                        ctx,
                        fullName.value,
                        email.value,
                        password.value,
                        UserType.student.name
                    )
                }
            if (res == null) {
                return@launch
            }
            delay(1000)
            // goto login
            if (!isLogin.value) {
                isLogin.value = true
            } else {
                targetApp.clearCache.value = !targetApp.clearCache.value
                done()

            }
        }

    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {

        CustomText("Welcome",Modifier
            .padding(top = 50.dp),26.sp,FontWeight.Bold)

        if (!isLogin.value) {
            TextField(
                modifier = Modifier
                    .padding(top = 50.dp)
                    .width(250.dp),
                value = fullName.value,
                onValueChange = { fullName.value = it },
                label = { Text("Full Name") },
                placeholder = { Text("Full Name") })
        }
        CustomTextField(email.value,{ email.value = it },"Email",Modifier
            .padding(top = 50.dp)
            .width(250.dp),{ Text("") })
        CustomTextField(password.value, {password.value = it },"Password",modifier = Modifier
            .padding(top = 50.dp)
            .width(250.dp),{ Text("") },PasswordVisualTransformation())

        CustomButton(handleSubmit,text = if (isLogin.value) "Login" else "Register",modifier = Modifier.padding(top = 50.dp).width(250.dp),true,10.dp)
        // text color should be grey
        CustomText(if (isLogin.value) "Don't have an account?" else "Already have an account?",Modifier
            .padding(top = 20.dp)
            .clickable {
                isLogin.value = !isLogin.value
            },
            MaterialTheme.typography.body1.fontSize,null,Color.Gray)

        GoogleSignInButton(onSignInSuccess = {
            Log.d(
                "email",
                "onSignInSuccess: ${it.idToken}"
            )
            coroutineScope.launch {
                val token = it.idToken
                val res: LoginResponse = userRepo.userGoogleLogin(ctx, token!!) ?: return@launch
                delay(1000)
                // goto login
                if (!isLogin.value) {
                    isLogin.value = true
                } else {
                    done()
                }
            }
        })
    }

}