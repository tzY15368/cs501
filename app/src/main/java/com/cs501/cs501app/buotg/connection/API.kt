package com.cs501.cs501app.buotg.connection
import com.cs501.cs501app.buotg.database.SyncData
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

interface API {

    @POST("login")
    @FormUrlEncoded
    suspend fun userLogin(
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<LoginResponse>

    @POST("register")
    @FormUrlEncoded
    suspend fun userSignup(
        @Field("full_name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("user_type") user_type: String
    ): Response<SignupResponse>

    @Headers("Content-Type: application/json")
    @POST("users")
    suspend fun sync(@Body syncData: SyncData): Response<SyncResponse>


    companion object {

        private val okkHttpClient = OkHttpClient.Builder()
            .build()

        fun getClient(
        ): API {

            return Retrofit.Builder()
                .client(okkHttpClient)
                .baseUrl("http://192.9.226.22:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(API::class.java)
        }

    }
}
