package com.cs501.cs501app.buotg.database.connection
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import java.io.IOException

open class SafeAPIRequest {
    suspend fun <T: Any> apiRequest(call: suspend() -> Response<T>) : T{

        val response = call.invoke()
        if (response.isSuccessful){
            return response.body()!!
        }else{
            val error = response.errorBody()?.string()

            val message = StringBuilder()
            error.let {
                try {
                    message.append(JSONObject(it).getString("msg"))
                }catch (e: JSONException){}
                message.append("\n")
            }
            message.append("Error Code: ${response.code()}")
            throw IOException(message.toString())
        }
    }
}