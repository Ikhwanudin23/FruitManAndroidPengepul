package com.one.fruitmanpengepul.ui.buyer_in_progress_activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.one.fruitmanpengepul.R
import com.one.fruitmanpengepul.adapters.collector.CollectorOrderinProgressAdapter
import com.one.fruitmanpengepul.models.Order
import com.one.fruitmanpengepul.utils.FruitmanUtil
import kotlinx.android.synthetic.main.activity_buyer_in_progress.*
import kotlinx.android.synthetic.main.content_buyer_in_progress.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class BuyerInProgressActivity : AppCompatActivity() {
    private val buyerInProgressViewModel : BuyerInProgressViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buyer_in_progress)
        setSupportActionBar(toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
        toolbar.setNavigationOnClickListener { finish() }
        setupUI()
        buyerInProgressViewModel.listenToUIState().observer(this, Observer { handleUIState(it) })
        buyerInProgressViewModel.listenToOrderInProgress().observe(this, Observer { handleData(it) })
    }

    private fun setupUI(){
        rv_in_progress.apply {
            layoutManager = LinearLayoutManager(this@BuyerInProgressActivity)
            adapter = CollectorOrderinProgressAdapter(mutableListOf(), this@BuyerInProgressActivity, buyerInProgressViewModel)
        }
    }

    private fun handleData(it: List<Order>){
        rv_in_progress.adapter?.let { a ->
            if(a is CollectorOrderinProgressAdapter){
                a.changelist(it)
            }
        }
    }

    private fun handleUIState(it: BuyerInProgressState){
        when(it){
            is BuyerInProgressState.IsLoading -> {
                if(it.state){
                    loading.visibility = View.VISIBLE
                }else{
                    loading.visibility = View.GONE
                }
            }
            is BuyerInProgressState.ShowToast -> toast(it.message)
        }
    }

    override fun onResume() {
        super.onResume()
        buyerInProgressViewModel.fetchOrderInProgress("Bearer ${FruitmanUtil.getToken(this)}")
    }

    private fun toast(message: String) = Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}