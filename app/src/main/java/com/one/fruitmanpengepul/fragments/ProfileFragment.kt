package com.one.fruitmanpengepul.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import coil.api.load
import com.one.fruitmanpengepul.ui.product_activity.ProductActivity
import com.one.fruitmanpengepul.R
import com.one.fruitmanpengepul.adapters.seller.SellerMyProductAdapter
import com.one.fruitmanpengepul.models.User
import com.one.fruitmanpengepul.utils.FruitmanUtil
import com.one.fruitmanpengepul.viewmodels.ProductState
import com.one.fruitmanpengepul.viewmodels.ProductViewModel
import com.one.fruitmanpengepul.viewmodels.UserViewModel
import com.one.fruitmanpengepul.webservices.ApiClient
import kotlinx.android.synthetic.main.fragment_profile.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : Fragment(R.layout.fragment_profile){
    private val productViewModel : ProductViewModel by viewModel()
    private val userViewModel : UserViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv_my_product.apply {
            adapter = SellerMyProductAdapter(mutableListOf(), requireActivity())
            layoutManager = LinearLayoutManager(requireActivity())
        }
        productViewModel.getState().observer(viewLifecycleOwner, Observer { handleUI(it) })
        productViewModel.getProducts().observe(viewLifecycleOwner, Observer {
            rv_my_product.adapter?.let { adapter ->
                if (adapter is SellerMyProductAdapter){
                    adapter.changelist(it)
                }
            }
        })
        userViewModel.profile("Bearer ${FruitmanUtil.getToken(requireActivity())}")
        userViewModel.listenToUser().observe(viewLifecycleOwner, Observer { handleDataUser(it) })
        fab.setOnClickListener { startActivity(Intent(activity, ProductActivity::class.java)) }
    }

    private fun handleDataUser(it : User){
        tv_name.text = it.name
        tv_email.text = it.email
        tv_address.text = it.address
        tv_telp.text = it.phone
        iv_image.load("${ApiClient.ENDPOINT}/uploads/user/${it.image}")
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
        productViewModel.getMyProducts("Bearer ${FruitmanUtil.getToken(requireActivity())}")
    }

    private fun toast(message : String) = Toast.makeText(requireActivity(), message, Toast.LENGTH_LONG).show()
}