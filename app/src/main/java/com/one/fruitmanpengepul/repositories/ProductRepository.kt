package com.one.fruitmanpengepul.repositories

import com.one.fruitmanpengepul.models.Product
import com.one.fruitmanpengepul.webservices.ApiService
import com.one.fruitmanpengepul.webservices.WrappedListResponse
import com.one.fruitmanpengepul.webservices.WrappedResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.Error


class ProductRepository (private val api : ApiService) {
    fun getAllProduct(token : String, result: (List<Product>?, Error?) -> Unit){
        api.getAllProduct(token).enqueue(object : Callback<WrappedListResponse<Product>> {
            override fun onFailure(call: Call<WrappedListResponse<Product>>, t: Throwable) {
                println("OnFailure : ${t.message}")
                result(null, Error(t.message.toString()))
            }

            override fun onResponse(call: Call<WrappedListResponse<Product>>, response: Response<WrappedListResponse<Product>>) {
                if (response.isSuccessful){
                    val body = response.body()
                    if (body?.status!!){
                        val data = body.data
                        result(data, null)
                    }else{
                        result(null, Error("Error with status code ${response.code()}"))
                    }
                }else{
                    result(null, Error("Error with status code ${response.code()}"))
                }
            }
        })
    }

    fun getMyProduct(token: String, result: (List<Product>?, Error?) -> Unit){
        api.getMyProduct(token).enqueue(object : Callback<WrappedListResponse<Product>> {
            override fun onFailure(call: Call<WrappedListResponse<Product>>, t: Throwable) {
                println("OnFailure : ${t.message}")
                result(null, Error(t.message.toString()))
            }

            override fun onResponse(call: Call<WrappedListResponse<Product>>, response: Response<WrappedListResponse<Product>>) {
                if (response.isSuccessful){
                    val body  = response.body()
                    if (body?.status!!){
                        val data = body.data
                        result(data, null)
                    }else{
                        result(null, Error(body.message))
                    }
                }else{
                    result(null, Error("Error with response code ${response.code()}"))
                }
            }
        })
    }

    fun createProduct(token: String, requestBody: HashMap<String, RequestBody>, image: MultipartBody.Part, result: (Boolean, Error?) -> Unit){
        api.createProduct(token, requestBody, image).enqueue(object : Callback<WrappedResponse<Product>>{
            override fun onFailure(call: Call<WrappedResponse<Product>>, t: Throwable) {
                println(t.message.toString())
                result(false, Error(t.message.toString()))
            }

            override fun onResponse(call: Call<WrappedResponse<Product>>, response: Response<WrappedResponse<Product>>) {
                if(response.isSuccessful){
                    val b = response.body()
                    if(b!!.status){
                        result(true, null)
                    }else{
                        result(false, Error(b.message))
                    }
                }else{
                    result(false, Error("Error with status code ${response.code()}"))
                }
            }
        })
    }
}
