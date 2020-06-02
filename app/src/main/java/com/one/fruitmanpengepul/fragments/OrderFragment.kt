package com.one.fruitmanpengepul.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.one.fruitmanpengepul.R
import com.one.fruitmanpengepul.ui.buyer_in_progress_activity.BuyerInProgressActivity
import com.one.fruitmanpengepul.ui.order_in_progress_activity.OrderInActivity
import com.one.fruitmanpengepul.ui.seller_in_progress_activity.SellerInProgressActivity
import com.one.fruitmanpengepul.ui.waiting_activity.WaitingOrderActivity
import kotlinx.android.synthetic.main.fragment_order.view.*

class OrderFragment : Fragment(R.layout.fragment_order){

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buttonBehavior()
    }

    private fun buttonBehavior(){
        with(requireView()){
            btn_seller_order_in.setOnClickListener {
                startActivity(Intent(requireActivity(), OrderInActivity::class.java))
            }

            btn_buyer_waiting.setOnClickListener {
                startActivity(Intent(requireActivity(), WaitingOrderActivity::class.java))
            }

            btn_seller_in_progress.setOnClickListener {
                startActivity(Intent(requireActivity(), SellerInProgressActivity::class.java))
            }

            btn_buyer_in_progress.setOnClickListener {
                startActivity(Intent(requireActivity(), BuyerInProgressActivity::class.java))

            }
        }
    }

}