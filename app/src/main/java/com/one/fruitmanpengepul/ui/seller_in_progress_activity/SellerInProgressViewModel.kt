package com.one.fruitmanpengepul.ui.seller_in_progress_activity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.one.fruitmanpengepul.models.Order
import com.one.fruitmanpengepul.repositories.OrderRepository
import com.one.fruitmanpengepul.utils.SingleLiveEvent

class SellerInProgressViewModel (private val orderRepository: OrderRepository) : ViewModel(){
    private val inProgressOrders = MutableLiveData<List<Order>>()
    private val state : SingleLiveEvent<SellerInProgressState> = SingleLiveEvent()

    private fun setLoading() { state.value = SellerInProgressState.IsLoading(true) }
    private fun hideLoading() { state.value = SellerInProgressState.IsLoading(false) }
    private fun toast(message: String) { state.value = SellerInProgressState.ShowToast(message) }

    fun fetchInProgressOrder(token: String){
        setLoading()
        orderRepository.sellerGetOrderInProgress(token){result, error->
            hideLoading()
            error?.let { toast(it.message.toString()) }
            result?.let { inProgressOrders.postValue(it) }
        }
    }

    fun completed(token: String, id: String){
        setLoading()
        orderRepository.completed(token, id){resultBool, error->
            hideLoading()
            error?.let { toast(it.message.toString()) }
            if(resultBool){ state.value = SellerInProgressState.SuccessReject }
        }
    }

    fun reject(token: String, orderId : Int, role : String, affectedFragment: Int){
        setLoading()
        orderRepository.decline(token, orderId, role){ resultBool, error ->
            hideLoading()
            error?.let { toast(it.message.toString()) }
            if(resultBool){ state.value = SellerInProgressState.SuccessReject }
        }
    }

    fun listenToState() = state
    fun listenToInProgressOrders() = inProgressOrders
}

sealed class SellerInProgressState{
    object SuccessConfirm : SellerInProgressState()
    object SuccessReject : SellerInProgressState()
    data class IsLoading(var state : Boolean) : SellerInProgressState()
    data class ShowToast(var message : String) : SellerInProgressState()
}