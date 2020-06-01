package com.one.fruitmanpengepul

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import coil.api.load
import com.fxn.pix.Pix
import com.one.fruitmanpengepul.models.Product
import com.one.fruitmanpengepul.utils.FruitmanUtil
import com.one.fruitmanpengepul.viewmodels.ProductState
import com.one.fruitmanpengepul.viewmodels.ProductViewModel
import kotlinx.android.synthetic.main.activity_product.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class ProductActivity : AppCompatActivity() {
    private val IMAGE_REQ_CODE = 101
    private val productViewModel: ProductViewModel by viewModel()
    private var imageUrl = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)
        btn_add_image.setOnClickListener { Pix.start(this@ProductActivity, IMAGE_REQ_CODE) }
        productViewModel.getState().observer(this@ProductActivity, Observer { handleUI(it) })
        fill()
    }

    private fun fill(){
        if (isInsert()){
            btn_submit.text = "insert"
            btn_submit.setOnClickListener {
                val token = "Bearer ${FruitmanUtil.getToken(this@ProductActivity)}"
                val name = et_name.text.toString().trim()
                val price = et_price.text.toString().trim()
                val desc = et_description.text.toString().trim()
                val address = et_address.text.toString().trim()
                if (productViewModel.validate(name, price, desc,  address, imageUrl)){
                    val productToSend = Product(name = name, price = price, address = address, description = desc)
                    productViewModel.createProduct(token, productToSend, imageUrl)
                }else{
                    showInfoAlert("Not valid")
                }
            }
        }else{
            btn_submit.text = "update"
            getProduct()
            btn_submit.setOnClickListener {
                val token = "Bearer ${FruitmanUtil.getToken(this@ProductActivity)}"
                val name = et_name.text.toString().trim()
                val price = et_price.text.toString().trim()
                val desc = et_description.text.toString().trim()
                val address = et_address.text.toString().trim()
                if (productViewModel.validate(name, price, desc,  address, null)){
                    val productToSend = Product(name = name, price = price, address = address, description = desc)
                    productViewModel.updateProduct(token, getPassedProduct()?.id.toString(), productToSend, imageUrl)
                }else{
                    showInfoAlert("Not valid")
                }
            }

        }
    }

    private fun getProduct(){
        getPassedProduct()?.let {
            et_name.setText(it.name.toString())
            et_price.setText((it.price.toString()))
            et_description.setText(it.description.toString())
            et_address.setText(it.address.toString())
        }
    }

    private fun handleUI(it : ProductState){
        when(it){
            is ProductState.ShowToast -> toast(it.message)
            is ProductState.IsLoading -> {
                btn_submit.isEnabled = !it.state
            }
            is ProductState.Success -> finish()
            is ProductState.Reset -> {
                setNameErr(null)
                setPriceErr(null)
                setAddressErr(null)
                setDescErr(null)
            }
            is ProductState.Validate -> {
                it.name?.let { setNameErr(it) }
                it.price?.let { setPriceErr(it) }
                it.address?.let { setAddressErr(it) }
                it.desc?.let { setDescErr(it) }
                it.image?.let { setImageErr(it) }
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
                imageUrl = it[0]
            }
        }
    }

    private fun showInfoAlert(message: String){
        AlertDialog.Builder(this).apply {
            setMessage(message)
            setPositiveButton(resources.getString(R.string.info_understand)){ d, _ -> d.dismiss()}
        }.show()
    }

    private fun isInsert() = intent.getBooleanExtra("IS_INSERT", true)
    private fun getPassedProduct() : Product? = intent.getParcelableExtra("PRODUCT")
    private fun setNameErr(err : String?) { til_name.error = err }
    private fun setPriceErr(err : String?) { til_price.error = err }
    private fun setAddressErr(err : String?) { til_address.error = err }
    private fun setDescErr(err : String?) { til_description.error = err }
    private fun setImageErr(err : String?) { showInfoAlert(err.toString()) }
    private fun toast(message : String) = Toast.makeText(this@ProductActivity, message, Toast.LENGTH_LONG).show()
}
