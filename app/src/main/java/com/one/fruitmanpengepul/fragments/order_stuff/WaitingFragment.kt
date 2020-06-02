package com.one.fruitmanpengepul.fragments.order_stuff

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.one.fruitmanpengepul.R
import com.one.fruitmanpengepul.adapters.collector.CollectorWaitingAdapter
import com.one.fruitmanpengepul.adapters.seller.SellerOrderInAdapter
import com.one.fruitmanpengepul.models.Order
import com.one.fruitmanpengepul.utils.FruitmanUtil
import com.one.fruitmanpengepul.viewmodels.OrderState
import com.one.fruitmanpengepul.viewmodels.OrderViewModel
import com.one.fruitmanpengepul.viewmodels.UserRole
import kotlinx.android.synthetic.main.fragment_waiting_confirmation.*
import kotlinx.android.synthetic.main.fragment_waiting_confirmation.view.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class WaitingFragment : Fragment(R.layout.fragment_waiting_confirmation){
    private val orderViewModel: OrderViewModel by sharedViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialize()
        orderViewModel.listenToRole().observe(viewLifecycleOwner, Observer { reSetupAdapter(it) })
        orderViewModel.listenToOrders().observe(viewLifecycleOwner, Observer { handleData(it) })

    }

    private fun initialize(){
        requireView().rv_waiting.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = CollectorWaitingAdapter(mutableListOf(), requireActivity(), orderViewModel)
        }
    }

    private fun handleData(it: List<Order>){
        rv_waiting.adapter?.let { adapter ->
            if (adapter is CollectorWaitingAdapter){
                adapter.changelist(it)
            }else if(adapter is SellerOrderInAdapter){
                adapter.changelist(it)
            }
        }
    }

    private fun reSetupAdapter(role: UserRole){
        requireView().rv_waiting.apply {
            adapter = if(role == UserRole.BUYER){
                CollectorWaitingAdapter(mutableListOf(), requireActivity(), orderViewModel)
            }else{
                SellerOrderInAdapter(mutableListOf(), requireActivity(), orderViewModel)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        fetch()
    }

    private fun fetch(){
        FruitmanUtil.getToken(requireActivity())?.let { token ->
            val t = "Bearer $token"
            val defaultValue = orderViewModel.listenToRole().value!!
            if(defaultValue == UserRole.BUYER){
                orderViewModel.collectorWaitingOrder(t)
            }else{
                orderViewModel.sellerGetOrderIn(t)
            }
        }
    }

    private fun toast(message : String) = Toast.makeText(activity!!, message, Toast.LENGTH_LONG).show()
}