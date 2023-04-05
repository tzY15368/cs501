package com.cs501.cs501app.buotg.connection
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface API {

    @POST("login")
    @FormUrlEncoded
    suspend fun userLogin(
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<LoginResponse>

    @POST("signup")
    @FormUrlEncoded
    suspend fun userSignup(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("user_type") user_type: String
    ): Response<SignupResponse>


    companion object {

        private val okkHttpClient = OkHttpClient.Builder()
            .build()

        fun getClient(
        ): API {

            return Retrofit.Builder()
                .client(okkHttpClient)
                .baseUrl("https://github.com/RickWayne1125/bu-on-the-go-backend/") //TODO:temp one, need to change
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(API::class.java)
        }

    }
}
