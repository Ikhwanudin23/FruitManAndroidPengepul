package com.one.fruitmanpengepul.webservices

import com.google.gson.annotations.SerializedName
import com.one.fruitmanpengepul.models.User
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

object ApiClient {
    private var retrofit : Retrofit? = null
    const val ENDPOINT = "https://fruitman-app.herokuapp.com/"
    private val opt = OkHttpClient.Builder().apply {
        connectTimeout(30, TimeUnit.SECONDS)
        readTimeout(30, TimeUnit.SECONDS)
        writeTimeout(30, TimeUnit.SECONDS)
    }.build()

    private fun getClient() : Retrofit {
        if(retrofit == null){
            retrofit = Retrofit.Builder().baseUrl(ENDPOINT).client(opt)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit!!
        }
        return retrofit!!
    }

    fun instance() = getClient().create(ApiService::class.java)
}

interface ApiService {
    @FormUrlEncoded
    @POST("api/user/login")
    fun login(@Field("email") email : String, @Field("password") password : String)
            : Call<WrappedResponse<User>>

    @FormUrlEncoded
    @POST("api/user/register")
    fun register(@Field("name") name : String, @Field("email") email : String, @Field("password") password: String)
            : Call<WrappedResponse<User>>
}

data class WrappedResponse<T>(
    @SerializedName("message") var message : String,
    @SerializedName("status") var status : Boolean,
    @SerializedName("data") var data : T? = null
)

data class WrappedListResponse<T>(
    @SerializedName("message") var message : String,
    @SerializedName("status") var status : Boolean,
    @SerializedName("data") var data : List<T>? = null
)