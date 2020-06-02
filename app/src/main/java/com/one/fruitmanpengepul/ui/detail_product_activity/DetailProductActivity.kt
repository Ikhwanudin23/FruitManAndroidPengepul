package com.one.fruitmanpengepul.ui.detail_product_activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import coil.api.load
import com.one.fruitmanpengepul.R
import com.one.fruitmanpengepul.models.Product
import com.one.fruitmanpengepul.utils.FruitmanUtil
import com.one.fruitmanpengepul.viewmodels.OrderState
import com.one.fruitmanpengepul.viewmodels.OrderViewModel
import kotlinx.android.synthetic.main.activity_detail_product.*
import kotlinx.android.synthetic.main.content_detail_product.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailProductActivity : AppCompatActivity() {
    private val orderViewModel: OrderViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_product)
        setSupportActionBar(toolbar)

        getPassedProduct()?.let {
            supportActionBar?.title = it.name
            iv_product.load(""+it.image)
            tv_name.text = it.user?.name
            tv_price.text = it.price.toString()
            tv_description.text = it.description
            tv_address.text = it.address

            btn_order.setOnClickListener {btn->
                val offer_price = et_offer_price.text.toString().trim()
                if (orderViewModel.validate(offer_price)){
                    orderViewModel.postOrder("Bearer ${FruitmanUtil.getToken(this@DetailProductActivity)!!}",
                        it.user?.id!!, it.id!!, offer_price)
                }
            }
        }
        orderViewModel.getState().observer(this, Observer { handleUI(it) })
    }

    private fun handleUI(it : OrderState){
        when(it){
            is OrderState.ShowToast -> toast(it.message)
            is OrderState.IsLoading -> btn_order.isEnabled = !it.state
            is OrderState.Reset -> setErrorOfferPrice(null)
            is OrderState.Validate -> it.offer_price.let { setErrorOfferPrice(it) }
            is OrderState.Success -> {
                toast(resources.getString(R.string.info_success))
                finish()
            }
            is OrderState.Failed -> toast("Failed")

        }
    }

    private fun setErrorOfferPrice(err : String?) {til_offer_price.error = err}

    private fun toast(message : String) = Toast.makeText(this, message, Toast.LENGTH_LONG).show()

    private fun getPassedProduct() : Product? = intent.getParcelableExtra("PRODUCT")
}
