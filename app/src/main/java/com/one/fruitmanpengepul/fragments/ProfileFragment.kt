package com.one.fruitmanpengepul.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.one.fruitmanpengepul.ProductActivity
import com.one.fruitmanpengepul.R
import com.one.fruitmanpengepul.adapters.seller.SellerMyProductAdapter
import com.one.fruitmanpengepul.utils.FruitmanUtil
import com.one.fruitmanpengepul.viewmodels.ProductState
import com.one.fruitmanpengepul.viewmodels.ProductViewModel
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment(R.layout.fragment_profile){
    private lateinit var productViewModel: ProductViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv_my_product.apply {
            adapter = SellerMyProductAdapter(mutableListOf(), activity!!)
            layoutManager = LinearLayoutManager(activity!!)
        }
        productViewModel = ViewModelProvider(this).get(ProductViewModel::class.java)
        productViewModel.getState().observer(viewLifecycleOwner, Observer { handleui(it) })
        productViewModel.getProducts().observe(viewLifecycleOwner, Observer {
            rv_my_product.adapter?.let { adapter ->
                if (adapter is SellerMyProductAdapter){
                    adapter.changelist(it)
                }
            }
        })
        fab.setOnClickListener { startActivity(Intent(activity, ProductActivity::class.java)) }
    }

    private fun handleui(it : ProductState){
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
        productViewModel.getMyProduct("Bearer ${FruitmanUtil.getToken(activity!!)}")
    }

    private fun toast(message : String) = Toast.makeText(activity!!, message, Toast.LENGTH_LONG).show()
}