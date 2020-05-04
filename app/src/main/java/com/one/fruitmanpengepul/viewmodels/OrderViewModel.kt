package com.one.fruitmanpengepul.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.one.fruitmanpengepul.models.OrderWaiting

class OrderViewModel : ViewModel(){
    private var orders = MutableLiveData<List<OrderWaiting>>()
    private var inPlaceOrders = MutableLiveData<List<String>>()
    fun fetchDataPalsu(){
        val x = mutableListOf<OrderWaiting>()
        for (i in 0..12){
            x.add(OrderWaiting("Menunggu konfirmasi dari pengguna $i"))
        }
        orders.postValue(x)
    }

    fun fetchDataPalsuInProgress(){
        val i = listOf(
            "Abu Muslih Assulkhani menunggu di tempat",
            "Fani Naditya Putra menunggu di tempat",
            "Den Baguse menunggu di tempat",
            "Ricardo Al Solim menunggu di tempat"
        )
        inPlaceOrders.postValue(i)
    }

    fun listenToInPlaceOrder() = inPlaceOrders
    fun listenToOrders() = orders
}