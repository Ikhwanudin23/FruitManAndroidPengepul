package com.one.fruitmanpengepul.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.one.fruitmanpengepul.R
import com.one.fruitmanpengepul.fragments.order_stuff.CompletedOrderFragment
import com.one.fruitmanpengepul.fragments.order_stuff.InProgressFragment
import com.one.fruitmanpengepul.fragments.order_stuff.WaitingFragment
import com.one.fruitmanpengepul.utils.CustomFragmentPagerAdapter
import com.one.fruitmanpengepul.viewmodels.OrderViewModel
import com.one.fruitmanpengepul.viewmodels.UserRole
import kotlinx.android.synthetic.main.fragment_order.view.*
import kotlinx.android.synthetic.main.fragment_order.view.fab
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class OrderFragment : Fragment(R.layout.fragment_order){
    private val orderViewModel: OrderViewModel by sharedViewModel()
    private lateinit var fragmentAdapter : CustomFragmentPagerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentAdapter = CustomFragmentPagerAdapter(childFragmentManager).apply {
            addFragment(WaitingFragment(), "Waiting")
            addFragment(InProgressFragment(), "In Progress")
            addFragment(CompletedOrderFragment(), "Completed")
        }
        view.viewpager.adapter = fragmentAdapter
        view.tabs.setupWithViewPager(view.viewpager)
        orderViewModel.listenToRole().observe(viewLifecycleOwner, Observer { handleSwitch(it) })
        switch()
    }

    private fun switch(){
        with(requireView()){
            fab.setOnClickListener {
                orderViewModel.switch()
            }
        }
    }

    private fun handleSwitch(role: UserRole){
        with(requireView()){
            if(role == UserRole.BUYER){
                fragmentAdapter.changeTitleAtPosition(0, resources.getString(R.string.title_waiting))
                view!!.tabs.getTabAt(0)?.setText(fragmentAdapter.getPageTitle(0))
                fab.text = resources.getString(R.string.switcher_buyer)
            }else{
                fragmentAdapter.changeTitleAtPosition(0, resources.getString(R.string.title_order_in))
                view!!.tabs.getTabAt(0)?.setText(fragmentAdapter.getPageTitle(0))
                fab.text = resources.getString(R.string.switcher_seller)
            }
        }
    }
}