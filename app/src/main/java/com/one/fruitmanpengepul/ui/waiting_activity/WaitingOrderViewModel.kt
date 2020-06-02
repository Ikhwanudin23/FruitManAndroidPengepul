package com.one.fruitmanpengepul.ui.waiting_activity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.one.fruitmanpengepul.models.Order
import com.one.fruitmanpengepul.repositories.OrderRepository
import com.one.fruitmanpengepul.utils.SingleLiveEvent
import com.one.fruitmanpengepul.viewmodels.OrderState

class WaitingOrderViewModel (private val orderRepository: OrderRepository): ViewModel(){
    private val waitingOrders = MutableLiveData<List<Order>>()
    private val state : SingleLiveEvent<WaitingOrderState> = SingleLiveEvent()

    private fun setLoading() { state.value = WaitingOrderState.IsLoading(true) }
    private fun hideLoading() { state.value = WaitingOrderState.IsLoading(false) }
    private fun toast(message: String) { state.value = WaitingOrderState.ShowToast(message) }

    fun fetchWaitingOrder(token: String){
        setLoading()
        orderRepository.collectorWaitingOrder(token){ result, e ->
            hideLoading()
            e?.let { er -> er.message?.let { toast(it) } }
            result?.let { waitingOrders.postValue(it) }
        }
    }

    fun reject(token: String, orderId : Int, role : String){
        setLoading()
        orderRepository.decline(token, orderId, role){ resultBool, error ->
            hideLoading()
            error?.let { toast(it.message.toString()) }
            if(resultBool){ state.value = WaitingOrderState.SuccessReject }
        }
    }

    fun listenToUIState() = state
    fun listenToWaitingOrders() = waitingOrders
}

sealed class WaitingOrderState {
    object SuccessConfirm : WaitingOrderState()
    object SuccessReject : WaitingOrderState()
    data class IsLoading(var state : Boolean) : WaitingOrderState()
    data class ShowToast(var message : String) : WaitingOrderState()
}