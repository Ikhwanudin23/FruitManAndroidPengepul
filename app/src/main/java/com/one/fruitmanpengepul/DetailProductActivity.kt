package com.one.fruitmanpengepul

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import coil.api.load
import com.one.fruitmanpengepul.models.Order
import com.one.fruitmanpengepul.models.Product
import com.one.fruitmanpengepul.utils.FruitmanUtil
import com.one.fruitmanpengepul.viewmodels.OrderState
import com.one.fruitmanpengepul.viewmodels.OrderViewModel
import kotlinx.android.synthetic.main.activity_detail_product.*
import kotlinx.android.synthetic.main.content_detail_product.*

class DetailProductActivity : AppCompatActivity() {
    private lateinit var orderViewModel: OrderViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_product)
        setSupportActionBar(toolbar)

        getPassedProduct()?.let {
            supportActionBar?.title = it.name
            iv_product.load(""+it.image)
            tv_name.text = it.user.name
            tv_price.text = it.price.toString()
            tv_description.text = it.description
            tv_address.text = it.address

            btn_order.setOnClickListener {btn->
                val offer_price = et_offer_price.text.toString().trim()
                if (orderViewModel.validate(offer_price)){
                    orderViewModel.postOrder("Bearer ${FruitmanUtil.getToken(this@DetailProductActivity)!!}",
                        it.user.id!!, it.id!!, offer_price)
                }
            }
        }
        orderViewModel = ViewModelProvider(this).get(OrderViewModel::class.java)
        orderViewModel.getState().observer(this, Observer { handleui(it) })
    }

    private fun handleui(it : OrderState){
        when(it){
            is OrderState.ShowToast -> toast(it.message)
            is OrderState.IsLoading -> {
                if (it.state){
                    btn_order.isEnabled = false
                }else{
                    btn_order.isEnabled = true
                }
            }
            is OrderState.Reset -> setErrorOfferPrice(null)
            is OrderState.Validate -> it.offer_price.let { setErrorOfferPrice(it) }

        }
    }

    private fun setErrorOfferPrice(err : String?) {til_offer_price.error = err}

    private fun toast(message : String) = Toast.makeText(this, message, Toast.LENGTH_LONG).show()

    private fun getPassedProduct() : Product? = intent.getParcelableExtra("PRODUCT")
}
