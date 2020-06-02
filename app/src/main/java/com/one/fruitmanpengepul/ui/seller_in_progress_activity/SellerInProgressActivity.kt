package com.one.fruitmanpengepul.ui.seller_in_progress_activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.one.fruitmanpengepul.R
import com.one.fruitmanpengepul.adapters.seller.SellerOrderInProgressAdapter
import com.one.fruitmanpengepul.models.Order
import com.one.fruitmanpengepul.utils.FruitmanUtil
import kotlinx.android.synthetic.main.activity_seller_in_progress.*
import kotlinx.android.synthetic.main.content_seller_in_progress.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class SellerInProgressActivity : AppCompatActivity() {
    private val sellerInProgressViewModel : SellerInProgressViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seller_in_progress)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { finish() }
        toolbar.setNavigationOnClickListener { finish() }
        setupUI()
        sellerInProgressViewModel.listenToInProgressOrders().observe(this, Observer { handleDate(it) })
        sellerInProgressViewModel.listenToState().observer(this, Observer { handleUIState(it) })
    }

    private fun setupUI(){
        rv_in_progress.apply {
            layoutManager = LinearLayoutManager(this@SellerInProgressActivity)
            adapter = SellerOrderInProgressAdapter(mutableListOf(), this@SellerInProgressActivity, sellerInProgressViewModel)
        }
    }

    private fun handleUIState(it: SellerInProgressState){
        when(it){
            is SellerInProgressState.IsLoading -> {
                if(it.state){
                    loading.visibility = View.VISIBLE
                }else{
                    loading.visibility = View.GONE
                }
            }
            is SellerInProgressState.SuccessReject -> {
                toast(resources.getString(R.string.success_rejected))
                sellerInProgressViewModel.fetchInProgressOrder("Bearer ${FruitmanUtil.getToken(this)}")
            }
            is SellerInProgressState.SuccessConfirm -> {
                toast(resources.getString(R.string.success_confirmed))
                sellerInProgressViewModel.fetchInProgressOrder("Bearer ${FruitmanUtil.getToken(this)}")
            }
            is SellerInProgressState.ShowToast -> toast(it.message)
        }
    }

    private fun handleDate(it: List<Order>){
        rv_in_progress.adapter?.let { adapter ->
            if(adapter is SellerOrderInProgressAdapter){
                adapter.changelist(it)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        sellerInProgressViewModel.fetchInProgressOrder("Bearer ${FruitmanUtil.getToken(this)}")
    }

    private fun toast(message: String) = Toast.makeText(this, message, Toast.LENGTH_LONG).show()

}