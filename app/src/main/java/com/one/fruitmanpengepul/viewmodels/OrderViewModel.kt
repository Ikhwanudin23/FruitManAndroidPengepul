package com.one.fruitmanpengepul.viewmodels

import androidx.lifecycle.ViewModel
import com.one.fruitmanpengepul.repositories.OrderRepository
import com.one.fruitmanpengepul.utils.SingleLiveEvent

class OrderViewModel(private val orderRepository : OrderRepository) : ViewModel(){
    private val state : SingleLiveEvent<OrderState> = SingleLiveEvent()

    private fun setLoading() { state.value = OrderState.IsLoading(true) }
    private fun hideLoading() { state.value = OrderState.IsLoading(false) }
    private fun toast(message: String) { state.value = OrderState.ShowToast(message) }

    fun postOrder(token: String, seller_id : Int, product_id : Int, offer_price: String){
        setLoading()
        orderRepository.postOrder(token, seller_id, product_id, offer_price) { _ , error ->
            hideLoading()
            error?.let { toast(it.message.toString()) } ?: kotlin.run {
                state.value = OrderState.Success
            }
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

}
sealed class OrderState{
    object Failed : OrderState()
    object Success : OrderState()
    data class IsLoading(var state : Boolean) : OrderState()
    data class ShowToast(var message : String) : OrderState()
    data class Validate(var offer_price : String) : OrderState()
    object Reset : OrderState()

}