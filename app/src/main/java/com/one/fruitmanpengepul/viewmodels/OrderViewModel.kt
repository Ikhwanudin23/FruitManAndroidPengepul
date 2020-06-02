package com.one.fruitmanpengepul.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.one.fruitmanpengepul.models.Order
import com.one.fruitmanpengepul.models.User
import com.one.fruitmanpengepul.repositories.OrderRepository
import com.one.fruitmanpengepul.utils.SingleLiveEvent
import com.one.fruitmanpengepul.webservices.ApiClient
import com.one.fruitmanpengepul.webservices.ApiService
import com.one.fruitmanpengepul.webservices.WrappedListResponse
import com.one.fruitmanpengepul.webservices.WrappedResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderViewModel(private val orderRepository : OrderRepository) : ViewModel(){
    private val orders = MutableLiveData<List<Order>>()
    private val state : SingleLiveEvent<OrderState> = SingleLiveEvent()
    private val userRole  = MutableLiveData(UserRole.BUYER)

    private fun setLoading() { state.value = OrderState.IsLoading(true) }
    private fun hideLoading() { state.value = OrderState.IsLoading(false) }
    private fun toast(message: String) { state.value = OrderState.ShowToast(message) }
    private fun successConfirmed() { state.value = OrderState.SuccessConfirmed }
    private fun successArrived() { state.value = OrderState.SuccessArrived }
    private fun successCompleted() { state.value = OrderState.SuccessCompleted }

    fun switch(){
        val currentRole = userRole.value!!
        if (currentRole == UserRole.BUYER){ userRole.value = UserRole.SELLER }else{ userRole.value = UserRole.BUYER }
    }

    fun postOrder(token: String, seller_id : Int, product_id : Int, offer_price: String){
        setLoading()
        orderRepository.postOrder(token, seller_id, product_id, offer_price) { _ , error ->
            hideLoading()
            error?.let { toast(it.message.toString()) } ?: kotlin.run {
                state.value = OrderState.Success
            }
        }
    }

    fun collectorWaitingOrder(token:String){
        setLoading()
        orderRepository.collectorWaitingOrder(token){ result, e ->
            hideLoading()
            e?.let { er -> er.message?.let { toast(it) } ?:  kotlin.run {  state.value = OrderState.Failed }}
            if(!result.isNullOrEmpty()){
                orders.postValue(result)
            }
        }
    }

    fun collectorGetOrderInProgress(token: String){
        setLoading()
        orderRepository.collectorOrderInProgress(token){result, error ->
            hideLoading()
            error?.let { toast(it.message.toString()) }
            result?.let { orders.postValue(it) }
        }
    }

    fun collectorGetOrderCompleted(token: String){
        setLoading()
        orderRepository.collectorOrderCompleted(token){result, error ->
            hideLoading()
            error?.let { toast(it.message.toString()) }
            result?.let { orders.postValue(it) }
        }
    }

    fun sellerGetOrderIn(token: String){
        setLoading()
        orderRepository.sellerGetOrderIn(token){ resultList, error ->
            hideLoading()
            error?.let { er -> er.message?.let { toast(it) } ?:  kotlin.run {  state.value = OrderState.Failed }}
            if(!resultList.isNullOrEmpty()){
                orders.postValue(resultList)
            }
        }
    }

    fun sellerGetOrderInProgress(token: String){
        setLoading()
        orderRepository.sellerGetOrderInProgress(token){result, error->
            hideLoading()
            println(error)
            println(result)
            error?.let { toast(it.message.toString()) }
            result?.let { orders.postValue(it) }
        }
    }

    fun sellerGetOrderCompleted(token: String){
        orderRepository.sellergetOrderCompleted(token){result, error ->
            error?.let { toast(it.message.toString()) }
            result?.let { orders.postValue(it) }
        }
    }

    fun reject(token: String, orderId : Int, role : String){
        setLoading()
        orderRepository.decline(token, orderId, role){ resultBool, error ->
            hideLoading()
            error?.let { toast(it.message.toString()) }
            if(resultBool){ state.value = OrderState.SuccessDelete }
        }
    }

    fun arrived(token: String, id: String){
        setLoading()
        orderRepository.arrived(token, id){result, error ->
            hideLoading()
            error?.let { toast(it.message.toString()) }
            result?.let { successArrived() }
        }
    }

    fun confirmed(token: String, id : String){
        setLoading()
        orderRepository.confirmed(token, id){result, error ->
            hideLoading()
            error?.let { toast(it.message.toString()) }
            result.let { successConfirmed() }
        }
    }

    fun completed(token: String, id: String){
        setLoading()
        orderRepository.completed(token, id){result, error->
            hideLoading()
            error?.let { toast(it.message.toString()) }
            result.let { successConfirmed() }
        }
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
    fun listenToRole() = userRole

}
sealed class OrderState{
    object SuccessArrived : OrderState()
    object SuccessDelete : OrderState()
    object SuccessConfirmed : OrderState()
    object SuccessCompleted : OrderState()
    object Failed : OrderState()
    object Success : OrderState()
    data class IsLoading(var state : Boolean) : OrderState()
    data class ShowToast(var message : String) : OrderState()
    data class Validate(var offer_price : String) : OrderState()
    object Reset : OrderState()

}

enum class UserRole {
    SELLER, BUYER
}