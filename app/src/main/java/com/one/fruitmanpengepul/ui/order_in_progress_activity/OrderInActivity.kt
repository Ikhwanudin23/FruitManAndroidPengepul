package com.one.fruitmanpengepul.ui.order_in_progress_activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.one.fruitmanpengepul.R
import com.one.fruitmanpengepul.adapters.seller.SellerOrderInAdapter
import com.one.fruitmanpengepul.models.Order
import com.one.fruitmanpengepul.utils.FruitmanUtil
import kotlinx.android.synthetic.main.activity_order_in.*
import kotlinx.android.synthetic.main.content_order_in.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class OrderInActivity : AppCompatActivity() {
    private val orderInViewModel: OrderInViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_in)
        setSupportActionBar(toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
        toolbar.setNavigationOnClickListener { finish() }
        setupUI()
        orderInViewModel.listenToOrdersIn().observe(this, Observer { handleOrderIn(it) })
        orderInViewModel.listenToUIState().observer(this, Observer { handleUIState(it) })
    }

    private fun setupUI(){
        rv_order_in.apply {
            layoutManager = LinearLayoutManager(this@OrderInActivity)
            adapter = SellerOrderInAdapter(mutableListOf(), this@OrderInActivity, orderInViewModel)
        }
    }

    private fun handleUIState(it: OrderInState){
        when(it){
            is OrderInState.IsLoading -> {
                if(it.state){
                    loading.visibility = View.VISIBLE
                }else{
                    loading.visibility = View.GONE
                }
            }
            is OrderInState.ShowToast -> toast(it.message)
            is OrderInState.SuccessConfirm -> {
                toast(resources.getString(R.string.success_confirmed))
                orderInViewModel.fetchOrderIn("Bearer ${FruitmanUtil.getToken(this)}")
            }
            is OrderInState.SuccessReject -> {
                toast(resources.getString(R.string.success_rejected))
                orderInViewModel.fetchOrderIn("Bearer ${FruitmanUtil.getToken(this)}")
            }
        }
    }

    private fun handleOrderIn(it: List<Order>){
        toast("Refreshing recycler....")
        rv_order_in.adapter?.let { adapter ->
            if(adapter is SellerOrderInAdapter){
                adapter.changelist(it)
                println("Recycler has been refreshed...")
                toast("Recycler has been refreshed...")
            }
        }
    }

    override fun onResume() {
        super.onResume()
        orderInViewModel.fetchOrderIn("Bearer ${FruitmanUtil.getToken(this)}")
    }

    private fun toast(message : String) = Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}