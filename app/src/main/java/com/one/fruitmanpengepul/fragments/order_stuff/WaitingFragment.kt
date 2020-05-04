package com.one.fruitmanpengepul.fragments.order_stuff

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.one.fruitmanpengepul.R
import com.one.fruitmanpengepul.adapters.CollectorWaitingAdapter
import com.one.fruitmanpengepul.models.OrderWaiting
import com.one.fruitmanpengepul.viewmodels.OrderViewModel
import kotlinx.android.synthetic.main.fragment_waiting_confirmation.view.*

class WaitingFragment : Fragment(R.layout.fragment_waiting_confirmation){
    private lateinit var orderViewModel: OrderViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        orderViewModel = ViewModelProvider(this).get(OrderViewModel::class.java)
        orderViewModel.listenToOrders().observe(viewLifecycleOwner, Observer { handle(it) })
        orderViewModel.fetchDataPalsu()

    }

    private fun setupUI(){
        view!!.rv_waiting.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = CollectorWaitingAdapter(mutableListOf(), activity!!)
        }
    }

    private fun handle(it: List<OrderWaiting>){
        view!!.rv_waiting.adapter?.let {x ->
            if (x is CollectorWaitingAdapter){
                x.updateList(it)
            }
        }
    }
}