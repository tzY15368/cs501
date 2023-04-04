package com.cs501.cs501app.buotg.database.connection
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface API {

    @FormUrlEncoded
    @POST("login")
    suspend fun userLogin(
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<AuthResponses>

    @FormUrlEncoded
    @POST("signup")
    suspend fun userSignup(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<AuthResponses>


    companion object {
        operator fun invoke(
        ): API {

            val okkHttpClient = OkHttpClient.Builder()
                .build()

            return Retrofit.Builder()
                .client(okkHttpClient)
                .baseUrl("https://github.com/RickWayne1125/bu-on-the-go-backend/") //TODO:temp one, need to change
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(API::class.java)
        }

    }
}
