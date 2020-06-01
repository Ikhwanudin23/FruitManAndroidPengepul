package com.one.fruitmanpengepul.repositories

import com.one.fruitmanpengepul.models.Order
import com.one.fruitmanpengepul.viewmodels.OrderState
import com.one.fruitmanpengepul.webservices.ApiService
import com.one.fruitmanpengepul.webservices.WrappedListResponse
import com.one.fruitmanpengepul.webservices.WrappedResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderRepository (private val api : ApiService){

    fun postOrder(token: String, seller_id : Int, product_id : Int, offer_price: String, result: (Boolean, Error?) -> Unit) {
        api.postOrder(token, seller_id, product_id, offer_price.toInt()).enqueue(object :
            Callback<WrappedResponse<Order>> {
            override fun onFailure(call: Call<WrappedResponse<Order>>, t: Throwable) {
                println("OnFailure : ${t.message}")
                result(false, Error(t.message.toString()))
            }

            override fun onResponse(call: Call<WrappedResponse<Order>>, response: Response<WrappedResponse<Order>>) {
                if (response.isSuccessful){
                    val body = response.body()
                    if (body?.status!!){
                        result(true, null)
                    }else{
                        result(false, Error("Tidak dapat membuat order"))
                    }
                }else{
                    result(false, Error("Tidak dapat membuat order. Code ${response.code()}"))
                }
            }
        })
    }



    fun collectorWaitingOrder(token: String, result: (List<Order>?, Error?) -> Unit){
        println(token)
        api.getOrdeWaitingrByCollector(token).enqueue(object : Callback<WrappedListResponse<Order>> {
            override fun onFailure(call: Call<WrappedListResponse<Order>>, t: Throwable) {
                println("OnFailure : ${t.message}")
                result(mutableListOf(), Error(t.message.toString()))
            }

            override fun onResponse(call: Call<WrappedListResponse<Order>>, response: Response<WrappedListResponse<Order>>) {
                if (response.isSuccessful){
                    val body = response.body()
                    if (body?.status!!){
                        val data = body.data
                        result(data, null)
                    }else{
                        result(null, Error(body.message))
                    }
                }else{
                    result(null, Error("Error with status code ${response.code()}"))

                }
            }
        })
    }



    fun sellerGetOrderIn(token: String, result: (List<Order>?, Error?) -> Unit){
        println(token)
        api.getOrderInBySeller(token).enqueue(object : Callback<WrappedListResponse<Order>> {
            override fun onFailure(call: Call<WrappedListResponse<Order>>, t: Throwable) {
                println("OnFailure : ${t.message}")
                result(mutableListOf(), Error(t.message.toString()))
            }

            override fun onResponse(call: Call<WrappedListResponse<Order>>, response: Response<WrappedListResponse<Order>>) {
                if (response.isSuccessful){
                    val body = response.body()
                    if (body?.status!!){
                        val data = body.data
                        result(data, null)
                    }else{
                        result(mutableListOf(), Error(body.message))
                    }
                }else{
                    result(mutableListOf(), Error("Error with status code ${response.code()}"))
                }
            }
        })
    }

    fun decline(token: String, id : Int, role : String, result: (Boolean, Error?) -> Unit){
        api.decline(token, id, role).enqueue(object : Callback<WrappedResponse<Order>> {
            override fun onFailure(call: Call<WrappedResponse<Order>>, t: Throwable) {
                println("OnFailure : ${t.message}")
                result(false, Error(t.message.toString()))
            }

            override fun onResponse(call: Call<WrappedResponse<Order>>, response: Response<WrappedResponse<Order>>) {
                if (response.isSuccessful){
                    val body = response.body()
                    if (body?.status!!){
                        result(true, null)
                    }else{
                        result(false, Error(body.message))
                    }
                }else{
                    result(false, Error("${response.code()}"))
                }
            }

        })
    }
}