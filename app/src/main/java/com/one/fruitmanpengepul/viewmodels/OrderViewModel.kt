package com.one.fruitmanpengepul.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.one.fruitmanpengepul.models.OrderWaiting

class OrderViewModel : ViewModel(){
    private var orders = MutableLiveData<List<OrderWaiting>>()

    fun fetchDataPalsu(){
        val x = listOf<OrderWaiting>(
            OrderWaiting("Menunggu konfirmasi dari Royhan IR"),
            OrderWaiting("Menunggu konfirmasi dari Royhan IR"),
            OrderWaiting("Menunggu konfirmasi dari Royhan IR"),
            OrderWaiting("Menunggu konfirmasi dari Royhan IR"),
            OrderWaiting("Menunggu konfirmasi dari Royhan IR"),
            OrderWaiting("Menunggu konfirmasi dari Royhan IR"),
            OrderWaiting("Menunggu konfirmasi dari Royhan IR"),
            OrderWaiting("Menunggu konfirmasi dari Royhan IR"),
            OrderWaiting("Menunggu konfirmasi dari Royhan IR")
        )
        orders.postValue(x)
    }

    fun listenToOrders() = orders
}