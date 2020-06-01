package com.one.fruitmanpengepul.webservices

import com.google.gson.annotations.SerializedName
import com.one.fruitmanpengepul.models.Order
import com.one.fruitmanpengepul.models.Product
import com.one.fruitmanpengepul.models.User
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.*
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

    @FormUrlEncoded
    @POST("api/order/{id}/decline")
    fun decline(
        @Header("Authorization") token : String,
        @Path("id")id : Int,
        @Field("role") role : String
    ) : Call<WrappedResponse<Order>>

    @GET("api/product")
    fun getAllProduct(@Header("Authorization") token : String) : Call<WrappedListResponse<Product>>

    @GET("api/product/show")
    fun getMyProduct(@Header("Authorization") token : String) : Call<WrappedListResponse<Product>>

    @Multipart
    @POST("api/product/store")
    fun postProduct (
        @Header("Authorization") token : String,
        @Part("name")name : String,
        @Part("price") price : Int,
        @Part("address") address : String,
        @Part("description") description : String,
        @Part("image") image : String
    ) : Call<WrappedResponse<Product>>

    @FormUrlEncoded
    @POST("api/order/store")
    fun postOrder(
        @Header("Authorization") token : String,
        @Field("seller_id") seller_id : Int,
        @Field("product_id") product_id : Int,
        @Field("offer_price") offer_price : Int
    ) : Call<WrappedResponse<Order>>

    @GET("api/order/collector/waiting")
    fun getOrdeWaitingrByCollector(
        @Header("Authorization") token : String
    ) : Call<WrappedListResponse<Order>>

    @GET("api/order/seller/orderin")
    fun getOrderInBySeller(
        @Header("Authorization") token : String
    ) : Call<WrappedListResponse<Order>>

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