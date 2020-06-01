package com.one.fruitmanpengepul

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import coil.api.load
import com.fxn.pix.Pix
import com.one.fruitmanpengepul.utils.FruitmanUtil
import com.one.fruitmanpengepul.viewmodels.ProductState
import com.one.fruitmanpengepul.viewmodels.ProductViewModel
import kotlinx.android.synthetic.main.activity_product.*
import java.io.File

class ProductActivity : AppCompatActivity() {
    private val IMAGE_REQ_CODE = 101
    private lateinit var productViewModel: ProductViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)
        btn_add_image.setOnClickListener { Pix.start(this@ProductActivity, IMAGE_REQ_CODE) }

        productViewModel = ViewModelProvider(this).get(ProductViewModel::class.java)
        productViewModel.getState().observer(this@ProductActivity, Observer { handleui(it) })
    }

    private fun fill(image : String){
        btn_submit.setOnClickListener {
            val token = "Bearer ${FruitmanUtil.getToken(this@ProductActivity)}"
            val name = et_name.text.toString().trim()
            val price = et_price.text.toString().trim()
            val desc = et_description.text.toString().trim()
            val address = et_address.text.toString().trim()

            if (productViewModel.validate(name, price, desc,  address, image)){
                //toast("validasi sukses")
                productViewModel.postProduct(token, name, price, address, desc, image)
            }
        }
    }

    private fun handleui(it : ProductState){
        when(it){
            is ProductState.ShowToast -> toast(it.message)
            is ProductState.IsLoading -> {
                if (it.state){
                    btn_submit.isEnabled = false
                }else{
                    btn_submit.isEnabled = true
                }
            }
            is ProductState.Success -> finish()
            is ProductState.Reset -> {
                setNameErr(null)
                setPriceErr(null)
                setAddressErr(null)
                setDescErr(null)
                //setImageErr(null)
            }
            is ProductState.Validate -> {
                it.name?.let { setNameErr(it) }
                it.price?.let { setPriceErr(it) }
                it.address?.let { setAddressErr(it) }
                it.desc?.let { setDescErr(it) }
                //it.image?.let { setImageErr(it) }
            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == IMAGE_REQ_CODE && resultCode == Activity.RESULT_OK && data != null){
            val selectedImageUri = data.getStringArrayListExtra(Pix.IMAGE_RESULTS)
            selectedImageUri?.let {
                //toast("result : ${it[0]}")
                //image = it[0]
                iv_product.load(File(it[0]))
                fill(it[0])
            }
        }
    }

    private fun setNameErr(err : String?) { til_name.error = err }
    private fun setPriceErr(err : String?) { til_price.error = err }
    private fun setAddressErr(err : String?) { til_address.error = err }
    private fun setDescErr(err : String?) { til_description.error = err }
    private fun setImageErr(err : String?) { til_image.error = err }
    private fun toast(message : String) = Toast.makeText(this@ProductActivity, message, Toast.LENGTH_LONG).show()
}
