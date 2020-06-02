package com.one.fruitmanpengepul.ui.waiting_activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.one.fruitmanpengepul.R
import com.one.fruitmanpengepul.adapters.collector.CollectorWaitingAdapter
import com.one.fruitmanpengepul.models.Order
import com.one.fruitmanpengepul.utils.FruitmanUtil
import kotlinx.android.synthetic.main.activity_waiting_order.*
import kotlinx.android.synthetic.main.content_waiting_order.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class WaitingOrderActivity : AppCompatActivity() {
    private val waitingOrderViewModel: WaitingOrderViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_waiting_order)
        setSupportActionBar(toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
        toolbar.setNavigationOnClickListener{ finish() }
        setupUI()
        waitingOrderViewModel.listenToUIState().observer(this, Observer { handleUIState(it) })
        waitingOrderViewModel.listenToWaitingOrders().observe(this, Observer { handleOrder(it) })
    }

    private fun setupUI(){
        rv_order_waiting.apply {
            layoutManager = LinearLayoutManager(this@WaitingOrderActivity)
            adapter = CollectorWaitingAdapter(mutableListOf(), this@WaitingOrderActivity, waitingOrderViewModel)
        }
    }

    private fun handleUIState(it: WaitingOrderState){
        when(it){
            is WaitingOrderState.IsLoading -> {
                if(it.state){
                    loading.visibility = View.VISIBLE
                }else{
                    loading.visibility = View.GONE
                }
            }
            is WaitingOrderState.SuccessReject -> {
                toast(resources.getString(R.string.success_rejected))
                waitingOrderViewModel.fetchWaitingOrder("Bearer ${FruitmanUtil.getToken(this)}")
            }
            is WaitingOrderState.ShowToast -> toast(it.message)
        }
    }

    private fun handleOrder(it: List<Order>){
        rv_order_waiting.adapter?.let { a ->
            if(a is CollectorWaitingAdapter){
                a.changelist(it)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        waitingOrderViewModel.fetchWaitingOrder("Bearer ${FruitmanUtil.getToken(this)}")
    }

    private fun toast(message: String) = Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}