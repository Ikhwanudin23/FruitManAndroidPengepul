package com.one.fruitmanpengepul.fragments.order_stuff

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.one.fruitmanpengepul.R
import com.one.fruitmanpengepul.adapters.collector.CollectorWaitingAdapter
import com.one.fruitmanpengepul.adapters.seller.SellerOrderInAdapter
import com.one.fruitmanpengepul.utils.FruitmanUtil
import com.one.fruitmanpengepul.viewmodels.OrderState
import com.one.fruitmanpengepul.viewmodels.OrderViewModel
import kotlinx.android.synthetic.main.fragment_waiting_confirmation.*
import kotlinx.android.synthetic.main.list_item_collector_waiting.*

class WaitingFragment : Fragment(R.layout.fragment_waiting_confirmation){
    private lateinit var orderViewModel: OrderViewModel
    //var change : Boolean = false
    companion object{
        var change : Boolean = false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        orderViewModel = ViewModelProvider(this).get(OrderViewModel::class.java)
        orderViewModel.getState().observer(viewLifecycleOwner, Observer { handleui(it) })
        collector()
        fab.setOnClickListener {
            if (!change){
                seller()
                change = true
            }else{
                collector()
                change = false
            }
        }
    }

    private fun collector() {
        rv_waiting.apply {
            layoutManager = LinearLayoutManager(activity!!)
            adapter = CollectorWaitingAdapter(mutableListOf(), activity!!, orderViewModel)
        }
        //orderViewModel = ViewModelProvider(this).get(OrderViewModel::class.java)
        orderViewModel.collectorWaitingOrder("Bearer ${FruitmanUtil.getToken(activity!!)}")
        //orderViewModel.getState().observer(viewLifecycleOwner, Observer { handleui(it) })
        orderViewModel.listenToOrders().observe(viewLifecycleOwner, Observer {
            rv_waiting.adapter?.let {adapter ->
                if (adapter is CollectorWaitingAdapter){
                    adapter.changelist(it)
                }
            }
        })
    }

    private fun seller(){
        rv_waiting.apply {
            layoutManager = LinearLayoutManager(activity!!)
            adapter = SellerOrderInAdapter(mutableListOf(), activity!!)
        }
        //orderViewModel = ViewModelProvider(this).get(OrderViewModel::class.java)
        orderViewModel.sellerGetOrderIn("Bearer ${FruitmanUtil.getToken(activity!!)}")
        //orderViewModel.getState().observer(viewLifecycleOwner, Observer { handleui(it) })
        orderViewModel.listenToOrders().observe(viewLifecycleOwner, Observer {
            rv_waiting.adapter?.let {adapter ->
                if (adapter is SellerOrderInAdapter){
                    adapter.changelist(it)
                }
            }
        })
    }

    private fun handleui(it : OrderState){
        when(it){
            is OrderState.ShowToast -> toast(it.message)
            is OrderState.IsLoading -> {
                if (it.state){
                    pb_waiting.isIndeterminate = true
                    pb_waiting.visibility = View.VISIBLE
                }else{
                    pb_waiting.isIndeterminate = false
                    pb_waiting.visibility = View.GONE
                }
            }
        }
    }

    private fun toast(message : String) = Toast.makeText(activity!!, message, Toast.LENGTH_LONG).show()
}