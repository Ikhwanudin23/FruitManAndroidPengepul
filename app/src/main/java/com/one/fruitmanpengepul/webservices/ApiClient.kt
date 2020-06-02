package com.one.fruitmanpengepul.webservices

import com.google.gson.annotations.SerializedName
import com.one.fruitmanpengepul.models.Order
import com.one.fruitmanpengepul.models.Product
import com.one.fruitmanpengepul.models.User
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
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

    @GET("api/user/profile")
    fun profile(@Header("Authorization") token : String) : Call<WrappedResponse<User>>

    @Multipart
    @POST("api/user/profile/update")
    fun updateprofile(@Header("Authorization") token : String,
                      @PartMap partMap : HashMap<String, RequestBody>,
                      @Part image : MultipartBody.Part
    ) : Call<WrappedResponse<User>>

    @GET("api/product")
    fun getAllProduct(@Header("Authorization") token : String) : Call<WrappedListResponse<Product>>

    @GET("api/product/show")
    fun getMyProduct(@Header("Authorization") token : String) : Call<WrappedListResponse<Product>>

    @Multipart
    @POST("api/product/store")
    fun createProduct(@Header("Authorization") token : String, @PartMap partMap:  HashMap<String, RequestBody>, @Part image : MultipartBody.Part) : Call<WrappedResponse<Product>>

    @Multipart
    @POST("api/product/{id}/update")
    fun updateProduct(@Header("Authorization") token : String,
                      @Path("id") id : Int,
                      @PartMap partMap:  HashMap<String, RequestBody>,
                      @Part image : MultipartBody.Part
    ) : Call<WrappedResponse<Product>>

    @GET("api/product/{id}/delete")
    fun deleteproduct(@Header("Authorization") token : String,
                      @Path("id") id : Int
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

    @GET("api/order/collector/inprogress")
    fun getOrderInProgressByCollector(
        @Header("Authorization") token : String
    ) : Call<WrappedListResponse<Order>>

    @GET("api/order/collector/completed")
    fun getOrderCompletedByCollector(
        @Header("Authorization") token : String
    ) : Call<WrappedListResponse<Order>>

    @GET("api/order/seller/orderin")
    fun getOrderInBySeller(
        @Header("Authorization") token : String
    ) : Call<WrappedListResponse<Order>>

    @GET("api/order/seller/inprogress")
    fun getOrderInprogressBySeller(
        @Header("Authorization") token : String
    ) : Call<WrappedListResponse<Order>>

    @GET("api/order/seller/completed")
    fun getOrderCompletedBySeller(
        @Header("Authorization") token : String
    ) : Call<WrappedListResponse<Order>>

    @FormUrlEncoded
    @POST("api/order/{id}/decline")
    fun decline(
        @Header("Authorization") token : String,
        @Path("id")id : Int, @Field("role") role : String
    ) : Call<WrappedResponse<Order>>

    @GET("api/order/{id}/confirmed")
    fun confirmed(
        @Header("Authorization") token : String,
        @Path("id") id : Int
    ) : Call<WrappedResponse<Order>>

    @GET("api/order/{id}/arrived")
    fun arrived(
        @Header("Authorization") token : String,
        @Path("id") id : Int
    ) : Call<WrappedResponse<Order>>

    @GET("api/order/{id}/completed")
    fun completed(
        @Header("Authorization") token : String,
        @Path("id") id : Int
    ) : Call<WrappedResponse<Order>>

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