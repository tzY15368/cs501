package com.cs501.cs501app.buotg.connection

import android.content.Context
import android.os.Looper
import com.cs501.cs501app.utils.TAlert
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import retrofit2.http.HTTP
import java.io.IOException

open class SafeAPIRequest {
    // returning nullable T so that we handle errors only once here.
    // to use the value of T, do T?.let{ ... }, see the ping example
    suspend fun <T : StdResponse> apiRequest(
        ctx: Context?,
        call: suspend () -> Response<T>,
        successPrompt: Boolean = true
    ): T? {
        try {
            val response = call.invoke()
            if (response.isSuccessful) {
                // if response is successful, we expect the operation to succeed FULLY
                // decode the response body from json and cast it to T
                // T is the type of the response body
                println("Success: ${response.body()}")
                if (successPrompt) {
                    ctx?.let {
                        TAlert.success(it, response.body()!!.message)
                    }
                }
                return response.body()
            } else {// otherwise decode the response body from json and look for the message field
                val error = response.errorBody()?.string()
                println("Error: $error")
                var message = StringBuilder()
                error.let {
                    try {
                        message.append(it?.let { it1 -> JSONObject(it1).getString("message") })
                    } catch (_: JSONException) {
                    }
                    message.append("\n")
                }
                message.append("Error Code: ${response.code()}")
                if (response.code() == 401) {
                    message.clear()
                    message.append("Authentication failed, did you log in?")
                }
                throw IOException(message.toString())
            }
        } catch (e: Exception) {
            println("Exception: $e")
            e.printStackTrace()
            e.message?.let {
                ctx?.let { it1 ->
                    TAlert.fail(it1, it)
                }
            }
            // throw e
            return null
        }
    }
}