package com.cs501.cs501app.buotg.connection
import com.cs501.cs501app.buotg.database.SyncData
import com.cs501.cs501app.buotg.database.entities.*
import com.cs501.cs501app.buotg.database.repositories.AppRepository
import okhttp3.Interceptor
import okhttp3.OkHttpClient

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

interface API {

    @GET("/")
    @FormUrlEncoded
    suspend fun getIndex(): Response<StdResponse>

    @POST("login")
    @FormUrlEncoded
    suspend fun userLogin(
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<LoginResponse>

    @POST("google_login")
    @FormUrlEncoded
    suspend fun userGoogleLogin(
        @Field("google_token") token: String
    ): Response<LoginResponse>

    @POST("register")
    @FormUrlEncoded
    suspend fun userSignup(
        @Field("full_name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("user_type") user_type: String
    ): Response<SignupResponse>

    @GET("/user")
    suspend fun getUser(@Query("user_id") id: UUID): Response<UserResponse>

    @GET("/event/{int:event_id}")
    @FormUrlEncoded
    suspend fun event_details():Response<EventResponse>

    @GET("/event/list")
    @FormUrlEncoded
    suspend fun event_list(): Response<EventsResponse>

    @POST("/event")
    @FormUrlEncoded
    suspend fun create_event(
        @Field("event_name") event_name: String,
        /** Should latitude and longtitude be in float?*/
        @Field("latitude") latitude: Long,
        @Field("longtitude") longtitude: Long,
        @Field("start_time") start_time: SimpleDateFormat,
        @Field("end_time") end_time: SimpleDateFormat,
        @Field("repeat_mode") repeat_mode: Int,
        @Field("Priority") priority: EventPriority,
        @Field("desc") desc: String
    ):Response<StdResponse>

    @DELETE("/event/{int:event_id}")
    @FormUrlEncoded
    suspend fun delete_event():Response<StdResponse>

    @GET("/shared_event/{int:event_id}")
    @FormUrlEncoded
    suspend fun get_shared_event():Response<SharedEventResponse>

    @POST("/shared_event/{int:event_id}")
    @FormUrlEncoded
    suspend fun create_shared_event():Response<StdResponse>

    @DELETE("/shared_event")
    @FormUrlEncoded
    suspend fun delete_shared_event(
        @Field("shared_event_id") shared_event_id: UUID
    ):Response<StdResponse>

    @GET("/shared_event_participance/{int:shared_event_id}/list")
    @FormUrlEncoded
    suspend fun shared_event_participance_list():Response<SEPsResponse>


    @POST("/shared_event_participance")
    @FormUrlEncoded
    suspend fun create_shared_event_participance(
        @Field("shared_event_id") shared_event_id: UUID,
        @Field("user_id") user_id: UUID,
        @Field("status") status: Status
    ):Response<StdResponse>

    @DELETE("/shared_event_participance")
    @FormUrlEncoded
    suspend fun delete_shared_event_participance(
        @Field("shared_event_id") shared_event_id: UUID,
        @Field("user_id") user_id: UUID,
    ):Response<StdResponse>

    @GET("/group/list")
    suspend fun group_list():Response<GroupListResponse>

    @GET("/group/{group_id}")
    suspend fun get_group(
        @Path("group_id") group_id: Int
    ):Response<GroupResponse>

    @GET("/group/{group_id}/list")
    suspend fun group_member_list(
        @Path("group_id") group_id: Int
    ):Response<GMLResponse>

    @POST("/group/{group_id}/list")
    @FormUrlEncoded
    suspend fun add_group_member(
        @Path("group_id") group_id: Int,
        @Field("user_id") user_id: UUID
    ): Response<StdResponse>

    @DELETE("/group/{group_id}/list")
    suspend fun remove_group_member(
        @Path("group_id") group_id: Int,
        @Query("user_id") user_id: UUID
    ):Response<StdResponse>

    @POST("/group")
    @FormUrlEncoded
    suspend fun create_group(
        @Field("group_name") group_name:String,
        @Field("desc") desc: String
    ):Response<StdResponse>

    @DELETE("/group/{group_id}'")
    suspend fun delete_group(
        @Path("group_id") group_id: Int
    ):Response<StdResponse>

    @Headers("Content-Type: application/json")
    @POST("/sync")
    suspend fun sync(@Body syncData: SyncData): Response<SyncResponse>

    @GET("/invite/listGroup")
    suspend fun invite_list_group(
        @Query("group_id") group_id: Int
    ):Response<InviteResponse>

    @GET("/invite/list")
    suspend fun invite_list():Response<InviteResponse>

    enum class InviteStatus{
        PENDING, SUCCESS, FAIL
    }

    @POST("/invite")
    @FormUrlEncoded
    suspend fun invite(
        @Field("group_id") group_id: Int,
        @Field("user_email") user_email: String,
        @Field("status") status: InviteStatus
    ):Response<StdResponse>

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
