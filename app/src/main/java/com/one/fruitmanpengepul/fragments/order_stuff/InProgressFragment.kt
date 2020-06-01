package com.one.fruitmanpengepul.fragments.order_stuff

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.one.fruitmanpengepul.R
import com.one.fruitmanpengepul.adapters.collector.CollectorInProgressInPlaceAdapter
import com.one.fruitmanpengepul.viewmodels.OrderViewModel
import kotlinx.android.synthetic.main.fragment_in_progress.view.*

class InProgressFragment : Fragment(R.layout.fragment_in_progress){
    private lateinit var orderViewModel: OrderViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        orderViewModel = ViewModelProvider(this).get(OrderViewModel::class.java)
        //orderViewModel.listenToInPlaceOrder().observe(viewLifecycleOwner, Observer { handle(it) })
        //orderViewModel.fetchDataPalsuInProgress()
    }

    private fun setupUI(){
        view!!.rv_in_progress.apply {
            layoutManager = LinearLayoutManager(activity!!)
            adapter = CollectorInProgressInPlaceAdapter(mutableListOf(), activity!!)
        }
    }

    private fun handle(it: List<String>){
        view!!.rv_in_progress.adapter?.let { adapter ->
            if(adapter is CollectorInProgressInPlaceAdapter){
                adapter.updateList(it)
            }
        }
    }
}