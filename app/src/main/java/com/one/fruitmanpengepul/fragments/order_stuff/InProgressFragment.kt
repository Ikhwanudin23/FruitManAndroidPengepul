package com.one.fruitmanpengepul.fragments.order_stuff

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.one.fruitmanpengepul.R
import com.one.fruitmanpengepul.adapters.collector.CollectorOrderinProgressAdapter
import com.one.fruitmanpengepul.adapters.seller.SellerOrderInProgressAdapter
import com.one.fruitmanpengepul.models.Order
import com.one.fruitmanpengepul.utils.FruitmanUtil
import com.one.fruitmanpengepul.viewmodels.OrderViewModel
import com.one.fruitmanpengepul.viewmodels.UserRole
import kotlinx.android.synthetic.main.fragment_in_progress.view.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class InProgressFragment : Fragment(R.layout.fragment_in_progress){
    private val orderViewModel: OrderViewModel by sharedViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialize()
        orderViewModel.listenToRole().observe(viewLifecycleOwner, Observer { reSetupAdapter(it) })
        orderViewModel.listenToOrders().observe(viewLifecycleOwner, Observer { handleData(it) })
    }

    private fun initialize(){
        view!!.rv_in_progress.apply {
            layoutManager = LinearLayoutManager(activity!!)
            adapter = CollectorOrderinProgressAdapter(mutableListOf(), activity!!, orderViewModel)
        }
    }

    private fun handleData(it: List<Order>){
        view!!.rv_in_progress.adapter?.let { adapter ->
            if(adapter is CollectorOrderinProgressAdapter){
                adapter.changelist(it)
            }else if(adapter is SellerOrderInProgressAdapter){
                adapter.changelist(it)
            }
        }
    }

    private fun reSetupAdapter(role : UserRole){
        view!!.rv_in_progress.apply {
            adapter = if (role == UserRole.BUYER){
                CollectorOrderinProgressAdapter(mutableListOf(), activity!!, orderViewModel)
            }else{
                SellerOrderInProgressAdapter(mutableListOf(), activity!!, orderViewModel)
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
                orderViewModel.collectorGetOrderInProgress(t)
            }else{
                orderViewModel.sellerGetOrderInProgress(t)
            }
        }
    }
}