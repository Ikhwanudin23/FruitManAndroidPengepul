package com.one.fruitmanpengepul.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.one.fruitmanpengepul.R
import com.one.fruitmanpengepul.adapters.TimelineAdapter
import com.one.fruitmanpengepul.utils.FruitmanUtil
import com.one.fruitmanpengepul.viewmodels.ProductState
import com.one.fruitmanpengepul.viewmodels.ProductViewModel
import kotlinx.android.synthetic.main.fragment_timeline.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class TimelineFragment : Fragment(R.layout.fragment_timeline){
    private val productViewModel : ProductViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv_timeline.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = TimelineAdapter(mutableListOf(), requireActivity())

        }
        productViewModel.getState().observer(viewLifecycleOwner, Observer { handleui(it) })
        productViewModel.getProducts().observe(viewLifecycleOwner, Observer {
            rv_timeline.adapter?.let {adapter ->
                if (adapter is TimelineAdapter){
                    adapter.changelist(it)
                }
            }
        })
    }

    private fun handleui(it : ProductState){
        when(it){
            is ProductState.IsLoading -> {
                if (it.state){
                    pb_timeline.isIndeterminate = true
                    pb_timeline.visibility = View.VISIBLE
                }else{
                    pb_timeline.isIndeterminate = false
                    pb_timeline.visibility = View.GONE
                }
            }
            is ProductState.ShowToast -> toast(it.message)
        }
    }

    override fun onResume() {
        super.onResume()
        productViewModel.getAllProduct("Bearer ${FruitmanUtil.getToken(activity!!)!!}")
    }

    private fun toast(message : String) = Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
}