package com.one.fruitmanpengepul.ui.buyer_in_progress_activity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.one.fruitmanpengepul.models.Order
import com.one.fruitmanpengepul.repositories.OrderRepository
import com.one.fruitmanpengepul.utils.SingleLiveEvent

class BuyerInProgressViewModel (private val orderRepository: OrderRepository) : ViewModel(){
    private val orderInProgress = MutableLiveData<List<Order>>()
    private val state : SingleLiveEvent<BuyerInProgressState> = SingleLiveEvent()

    private fun setLoading() { state.value = BuyerInProgressState.IsLoading(true) }
    private fun hideLoading() { state.value = BuyerInProgressState.IsLoading(false) }
    private fun toast(message: String) { state.value = BuyerInProgressState.ShowToast(message) }

    fun fetchOrderInProgress(token: String){
        setLoading()
        orderRepository.collectorOrderInProgress(token){result, error ->
            hideLoading()
            error?.let { toast(it.message.toString()) }
            result?.let { orderInProgress.postValue(it) }
        }
    }

    fun arrived(token: String, id: String){
        setLoading()
        orderRepository.arrived(token, id){resultBool, error ->
            hideLoading()
            error?.let { toast(it.message.toString()) }
            if(resultBool){
                fetchOrderInProgress(token)
                toast("Arrived success")
            }
        }
    }

    fun completed(token: String, id: String){
        setLoading()
        orderRepository.completed(token, id){result, error->
            hideLoading()
            error?.let { toast(it.message.toString()) }
            if(result){
                fetchOrderInProgress(token)
                toast("Completed")
            }
        }
    }



    fun listenToUIState() = state
    fun listenToOrderInProgress() = orderInProgress


}

sealed class BuyerInProgressState {
    data class IsLoading(var state : Boolean) : BuyerInProgressState()
    data class ShowToast(var message : String) : BuyerInProgressState()
}