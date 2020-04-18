package com.one.fruitmanpengepul.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.one.fruitmanpengepul.R
import com.one.fruitmanpengepul.fragments.order_stuff.CompletedOrderFragment
import com.one.fruitmanpengepul.fragments.order_stuff.InProgressFragment
import com.one.fruitmanpengepul.fragments.order_stuff.WaitingFragment
import com.one.fruitmanpengepul.utils.CustomFragmentPagerAdapter
import kotlinx.android.synthetic.main.fragment_order.view.*

class OrderFragment : Fragment(R.layout.fragment_order){
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fragmentAdapter = CustomFragmentPagerAdapter(childFragmentManager).apply {
            addFragment(WaitingFragment(), "Waiting")
            addFragment(InProgressFragment(), "In Progress")
            addFragment(CompletedOrderFragment(), "Completed")
        }
        view.viewpager.adapter = fragmentAdapter
        view.tabs.setupWithViewPager(view.viewpager)
    }
}