package com.one.fruitmanpengepul.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.one.fruitmanpengepul.ProductActivity
import com.one.fruitmanpengepul.R
import com.one.fruitmanpengepul.adapters.seller.SellerMyProductAdapter
import com.one.fruitmanpengepul.utils.FruitmanUtil
import com.one.fruitmanpengepul.viewmodels.ProductState
import com.one.fruitmanpengepul.viewmodels.ProductViewModel
import kotlinx.android.synthetic.main.fragment_profile.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : Fragment(R.layout.fragment_profile){
    private val productViewModel : ProductViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv_my_product.apply {
            adapter = SellerMyProductAdapter(mutableListOf(), activity!!)
            layoutManager = LinearLayoutManager(activity!!)
        }
        productViewModel.getState().observer(viewLifecycleOwner, Observer { handleUI(it) })
        productViewModel.getProducts().observe(viewLifecycleOwner, Observer {
            rv_my_product.adapter?.let { adapter ->
                if (adapter is SellerMyProductAdapter){
                    adapter.changelist(it)
                }
            }
        })
        fab.setOnClickListener { startActivity(Intent(activity, ProductActivity::class.java)) }
    }

    private fun handleUI(it : ProductState){
        when(it){
            is ProductState.IsLoading -> {
                if (it.state){
                    pb_my_product.visibility = View.VISIBLE
                    pb_my_product.isIndeterminate = true
                }else{
                    pb_my_product.visibility = View.GONE
                    pb_my_product.isIndeterminate = false
                }
            }
            is ProductState.ShowToast -> toast(it.message)
        }
    }

    override fun onResume() {
        super.onResume()
        productViewModel.getMyProducts("Bearer ${FruitmanUtil.getToken(activity!!)}")
    }

    private fun toast(message : String) = Toast.makeText(activity!!, message, Toast.LENGTH_LONG).show()
}