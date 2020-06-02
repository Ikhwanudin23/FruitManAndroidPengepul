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

    fun collectorOrderInProgress(token: String, result: (List<Order>?, Error?) -> Unit){
        api.getOrderInProgressByCollector(token).enqueue(object : Callback<WrappedListResponse<Order>>{
            override fun onFailure(call: Call<WrappedListResponse<Order>>, t: Throwable) {
                println(t.message)
                result(null, Error(t.message))
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
                    result(null, Error(response.message()))
                }
            }

        })
    }

    fun collectorOrderCompleted(token: String, result: (List<Order>?, Error?) -> Unit){
        api.getOrderCompletedByCollector(token).enqueue(object : Callback<WrappedListResponse<Order>>{
            override fun onFailure(call: Call<WrappedListResponse<Order>>, t: Throwable) {
                println(t.message)
                result(null, Error(t.message))
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
                    result(null, Error(response.message()))
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

    fun sellerGetOrderInProgress(token: String, result: (List<Order>?, Error?) -> Unit){
        api.getOrderInprogressBySeller(token).enqueue(object : Callback<WrappedListResponse<Order>>{
            override fun onFailure(call: Call<WrappedListResponse<Order>>, t: Throwable) {
                println(t.message)
                result(null, Error(t.message))
            }

            override fun onResponse(call: Call<WrappedListResponse<Order>>, response: Response<WrappedListResponse<Order>>) {
                if (response.isSuccessful){
                    val body = response.body()
                    if (body?.status!!){
                        val data = body.data
                        result(data, null)
                        println(data)
                    }else{
                        println(body.message)
                        result(null, Error(body.message))
                    }
                }else{
                    println(response.message())
                    result(null, Error(response.message()))
                }
            }

        })
    }

    fun sellergetOrderCompleted(token: String, result: (List<Order>?, Error?) -> Unit){
        api.getOrderCompletedBySeller(token).enqueue(object : Callback<WrappedListResponse<Order>>{
            override fun onFailure(call: Call<WrappedListResponse<Order>>, t: Throwable) {
                println(t.message)
                result(null, Error(t.message))
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
                    result(null, Error(response.message()))
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

    fun confirmed(token: String, id : String, result : (Boolean, Error?) -> Unit){
        api.confirmed(token, id.toInt()).enqueue(object : Callback<WrappedResponse<Order>>{
            override fun onFailure(call: Call<WrappedResponse<Order>>, t: Throwable) {
                println(t.message)
                result(false, Error(t.message))
            }

            override fun onResponse(call: Call<WrappedResponse<Order>>, response: Response<WrappedResponse<Order>>) {
                if (response.isSuccessful){
                    val body = response.body()
                    if (body?.status!!){
                        result(true, null)
                    }else{
                        println(body.message)
                        result(false, Error(body.message))
                    }
                }else{
                    println(response.message())
                    result(false, Error(response.message()))
                }
            }

        })
    }

    fun arrived(token: String, id : String, result: (Boolean, Error?) -> Unit){
        api.arrived(token, id.toInt()).enqueue(object : Callback<WrappedResponse<Order>>{
            override fun onFailure(call: Call<WrappedResponse<Order>>, t: Throwable) {
                println(t.message)
                result(false, Error(t.message))
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
                    result(false, Error(response.message()))
                }
            }

        })
    }

    fun completed(token: String, id : String, result: (Boolean, Error?) -> Unit){
        api.completed(token, id.toInt()).enqueue(object : Callback<WrappedResponse<Order>>{
            override fun onFailure(call: Call<WrappedResponse<Order>>, t: Throwable) {
                println(t.message)
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
                    result(false, Error(response.message()))
                }
            }

        })
    }
}