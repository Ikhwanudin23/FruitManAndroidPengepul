package com.one.fruitmanpengepul.ui.order_in_progress_activity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.one.fruitmanpengepul.models.Order
import com.one.fruitmanpengepul.repositories.OrderRepository
import com.one.fruitmanpengepul.utils.SingleLiveEvent

class OrderInViewModel (private val orderRepository: OrderRepository) : ViewModel(){
    private val ordersIn = MutableLiveData<List<Order>>()
    private val state : SingleLiveEvent<OrderInState> = SingleLiveEvent()

    private fun setLoading(){ state.value = OrderInState.IsLoading(true) }
    private fun hideLoading(){ state.value = OrderInState.IsLoading(false) }
    private fun toast(message: String) { state.value = OrderInState.ShowToast(message) }


    fun fetchOrderIn(token: String) {
        setLoading()
        orderRepository.sellerGetOrderIn(token) { resultList, error ->
            hideLoading()
            error?.let { er ->
                er.message?.let { toast(it) }
            }
            resultList?.let {
                ordersIn.postValue(resultList)
            }
        }
    }

    fun reject(token: String, orderId : Int, role : String){
        setLoading()
        orderRepository.decline(token, orderId, role){ resultBool, error ->
            hideLoading()
            error?.let { toast(it.message.toString()) }
            if(resultBool){
                fetchOrderIn(token)
                state.value = OrderInState.SuccessReject
            }
        }
    }

    fun confirmed(token: String, id : String){
        setLoading()
        orderRepository.confirmed(token, id){ resultBool , error ->
            hideLoading()
            error?.let { toast(it.message.toString()) }
            if(resultBool){
                state.value = OrderInState.SuccessConfirm
            }
        }
    }

    fun listenToUIState() = state
    fun listenToOrdersIn() = ordersIn

}

sealed class OrderInState {
    object SuccessConfirm : OrderInState()
    object SuccessReject : OrderInState()
    data class IsLoading(var state : Boolean) : OrderInState()
    data class ShowToast(var message : String) : OrderInState()
}