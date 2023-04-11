package com.cs501.cs501app.buotg.connection
import com.cs501.cs501app.buotg.database.SyncData
import com.cs501.cs501app.buotg.database.entities.USER_TOKEN_KEY
import com.cs501.cs501app.buotg.database.repositories.AppRepository
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

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

    @POST("ping")
    suspend fun ping(): Response<StdResponse>

    companion object {

        private val clientBuilder = OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.SECONDS)

        suspend fun getClient(
        ): API {
            val token = AppRepository.get().kvDao().get(USER_TOKEN_KEY)?.value
            println("got token: $token")
            clientBuilder.addInterceptor(object: Interceptor {
                override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
                    val req = chain.request().newBuilder().removeHeader("Authorization")
                        .addHeader("Authorization",token.toString()).build()
                    return chain.proceed(req)
                }

            })
            return Retrofit.Builder()
                .client(clientBuilder.build())
                .baseUrl("http://192.9.226.22:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(API::class.java)
        }

    }
}
