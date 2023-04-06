package com.cs501.cs501app.buotg.view.user_setup

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cs501.cs501app.buotg.database.repositories.AppRepository
import com.cs501.cs501app.buotg.connection.StdResponse
import com.cs501.cs501app.buotg.database.entities.UserType
import com.cs501.cs501app.utils.TAlert
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun LoginRegister(done: () -> Unit = {}, msg: String = "") {
    val ctx = LocalContext.current
    val userRepo = AppRepository.get().userRepo()
    val coroutineScope = rememberCoroutineScope()
    val fullName = rememberSaveable { mutableStateOf("") }
    val email = rememberSaveable { mutableStateOf("") }
    val password = rememberSaveable { mutableStateOf("") }
    val isLogin = rememberSaveable { mutableStateOf(true) }
    Log.d("LoginRegister", "rendered: msg=$msg")

    LaunchedEffect(true) {
        // FIXME: room breaks
//        val user = userRepo.getCurrentUser()
//        if (user != null) {
//            Log.d("LoginRegister", "logging out user: $user")
//            AppRepository.get().userRepo().logout()
//        }
    }

    val handleSubmit: () -> Unit = {
        coroutineScope.launch {
            if (email.value.isEmpty() || password.value.isEmpty()) {
                TAlert.fail(ctx, "Please enter your email and password")
                return@launch
            }
            try{
                var res: StdResponse = if (isLogin.value) {
                    userRepo.userLogin(email.value, password.value)
                } else {
                    userRepo.userSignup(
                        fullName.value,
                        email.value,
                        password.value,
                        UserType.student.name
                    )
                }
                TAlert.success(ctx, res.message)
                delay(1000)
                // goto login
                if(!isLogin.value){
                    isLogin.value = true
                }
            } catch (e: Exception) {
                TAlert.fail(ctx, e.message?:"An error occurred")
                Log.e("LoginRegister", "error: ${e.message}", e)
                return@launch
            }
        }

    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {

        Text(
            text = "Welcome",
            modifier = Modifier
                .padding(top = 50.dp),
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 26.sp
            )
        )
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
        TextField(
            modifier = Modifier
                .padding(top = 50.dp)
                .width(250.dp),
            value = email.value,
            onValueChange = { email.value = it },
            label = { Text("Email") },
            placeholder = { Text("Email") })
        TextField(
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .padding(top = 50.dp)
                .width(250.dp),
            value = password.value,
            onValueChange = { password.value = it },
            label = { Text("Password") })
        Button(
            modifier = Modifier
                .padding(top = 50.dp)
                .width(250.dp),
            shape = RoundedCornerShape(10),
            onClick = handleSubmit
        ) {
            Text(text = if (isLogin.value) "Login" else "Register")
        }
        // text color should be grey
        Text(
            text = if (isLogin.value) "Don't have an account?" else "Already have an account?",
            modifier = Modifier
                .padding(top = 20.dp)
                .clickable {
                    isLogin.value = !isLogin.value
                },
            color = Color.Gray
        )
    }

}
