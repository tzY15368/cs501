package com.cs501.cs501app.buotg.connection
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import java.io.IOException

open class SafeAPIRequest {
    suspend fun <T: StdResponse> apiRequest(call: suspend() -> Response<T>) : T{

        val response = call.invoke()
        if (response.isSuccessful) {
            // if response is successful, we expect the operation to succeed FULLY
            // decode the response body from json and cast it to T
            // T is the type of the response body
            return response.body()!!
        }else{
            // otherwise decode the response body from json and look for the message field
            val error = response.errorBody()?.string()

            val message = StringBuilder()
            error.let {
                try {
                    message.append(it?.let { it1 -> JSONObject(it1).getString("message") })
                }catch (_: JSONException){}
                message.append("\n")
            }
            message.append("Error Code: ${response.code()}")
            throw IOException(message.toString())
        }
    }
}