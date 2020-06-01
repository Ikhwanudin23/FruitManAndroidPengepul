package com.one.fruitmanpengepul.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.one.fruitmanpengepul.models.Order
import com.one.fruitmanpengepul.models.User
import com.one.fruitmanpengepul.utils.SingleLiveEvent
import com.one.fruitmanpengepul.webservices.ApiClient
import com.one.fruitmanpengepul.webservices.WrappedListResponse
import com.one.fruitmanpengepul.webservices.WrappedResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderViewModel : ViewModel(){
    private var orders = MutableLiveData<List<Order>>()
    private var state : SingleLiveEvent<OrderState> = SingleLiveEvent()
    private var api = ApiClient.instance()

    fun postOrder(token : String, seller_id : Int, product_id : Int, offer_price: String){
        println("Bearer ${token}")
        println("offer price ${offer_price}")
        state.value = OrderState.IsLoading(true)
        api.postOrder(token, seller_id, product_id, offer_price.toInt()).enqueue(object : Callback<WrappedResponse<Order>>{
            override fun onFailure(call: Call<WrappedResponse<Order>>, t: Throwable) {
                println("OnFailure : ${t.message}")
                state.value = OrderState.IsLoading(false)
            }

            override fun onResponse(call: Call<WrappedResponse<Order>>, response: Response<WrappedResponse<Order>>) {
                if (response.isSuccessful){
                    val body = response.body()
                    if (body?.status!!){
                        state.value = OrderState.ShowToast("berhasil mengorder gatau ")
                    }else{
                        state.value = OrderState.ShowToast("body not found ${body.message}")
                        println("body not found ${body.message}")
                    }

                }else{
                    state.value = OrderState.ShowToast("not response ${response.message()}")
                    println("not response ${response.message()}")
                }
                state.value = OrderState.IsLoading(false)
            }

        })
    }

    fun collectorWaitingOrder(token: String){
        state.value = OrderState.IsLoading(true)
        api.getOrdeWaitingrByCollector(token).enqueue(object : Callback<WrappedListResponse<Order>>{
            override fun onFailure(call: Call<WrappedListResponse<Order>>, t: Throwable) {
                println("OnFailure : ${t.message}")
                state.value = OrderState.IsLoading(false)
            }

            override fun onResponse(call: Call<WrappedListResponse<Order>>, response: Response<WrappedListResponse<Order>>) {
                if (response.isSuccessful){
                    val body = response.body()
                    if (body?.status!!){
                        val data = body.data
                        orders.postValue(data)
                    }else{
                        println("status false ${body.message}")
                        state.value = OrderState.ShowToast("status false ${body.message}")
                    }
                }else{
                    println("tidak mendapat response ${response.message()}")
                    state.value = OrderState.ShowToast("tidak mendapat response ${response.message()}")
                }
                state.value = OrderState.IsLoading(false)
            }

        })
    }

    fun sellerGetOrderIn(token: String){
        state.value = OrderState.IsLoading(true)
        api.getOrderInBySeller(token).enqueue(object : Callback<WrappedListResponse<Order>>{
            override fun onFailure(call: Call<WrappedListResponse<Order>>, t: Throwable) {
                println("OnFailure : ${t.message}")
                state.value = OrderState.IsLoading(false)
            }

            override fun onResponse(call: Call<WrappedListResponse<Order>>, response: Response<WrappedListResponse<Order>>) {
                if (response.isSuccessful){
                    val body = response.body()
                    if (body?.status!!){
                        val data = body.data
                        orders.postValue(data)
                    }else{
                        println("status false ${body.message}")
                        state.value = OrderState.ShowToast("status false ${body.message}")
                    }
                }else{
                    println("tidak mendapat response ${response.message()}")
                    state.value = OrderState.ShowToast("tidak mendapat response ${response.message()}")
                }
                state.value = OrderState.IsLoading(false)
            }

        })
    }

    fun decline(token: String, id : Int, role : String){
        state.value = OrderState.IsLoading(true)
        api.decline(token, id, role).enqueue(object : Callback<WrappedResponse<Order>>{
            override fun onFailure(call: Call<WrappedResponse<Order>>, t: Throwable) {
                println("OnFailure : ${t.message}")
                state.value = OrderState.IsLoading(false)
            }

            override fun onResponse(call: Call<WrappedResponse<Order>>, response: Response<WrappedResponse<Order>>) {
                if (response.isSuccessful){
                    val body = response.body()
                    if (body?.status!!){
                        state.value = OrderState.ShowToast("berhasil menolak pesanan")
                        println("berhasil menolak pesanan")
                    }else{
                        println("status false ${body.message}")
                        state.value = OrderState.ShowToast("status false ${body.message}")
                    }
                }else{
                    println("not response ${response.message()}")
                    state.value = OrderState.ShowToast("not response ${response.message()}")
                }
                state.value = OrderState.IsLoading(false)
            }

        })
    }

    fun validate(offer_price: String) : Boolean{
        if (offer_price.isEmpty()){
            state.value = OrderState.Reset
            return false
        }
        return true
    }

    fun getState() = state
    fun listenToOrders() = orders
}
sealed class OrderState{
    data class IsLoading(var state : Boolean = false) : OrderState()
    data class ShowToast(var message : String) : OrderState()
    data class Validate(var offer_price : String) : OrderState()
    object Reset : OrderState()
}