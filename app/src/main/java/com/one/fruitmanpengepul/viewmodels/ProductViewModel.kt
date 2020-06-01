package com.one.fruitmanpengepul.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.one.fruitmanpengepul.models.Product
import com.one.fruitmanpengepul.repositories.ProductRepository
import com.one.fruitmanpengepul.utils.SingleLiveEvent
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.lang.Exception

class ProductViewModel(private val productRepository: ProductRepository) : ViewModel(){
    private var products = MutableLiveData<List<Product>>()
    private var state : SingleLiveEvent<ProductState> = SingleLiveEvent()

    private fun setLoading() { state.value = ProductState.IsLoading(true) }
    private fun hideLoading() { state.value = ProductState.IsLoading(false) }
    private fun toast(message: String) {state.value = ProductState.ShowToast(message)}
    private fun success() { state.value = ProductState.Success}

    fun getAllProduct(token:String){
        setLoading()
        productRepository.getAllProduct(token){resultList, e ->
            hideLoading()
            e?.let { toast(it.message.toString()) }
            resultList?.let { products.postValue(it) }
        }
    }

    fun getMyProducts(token: String){
        setLoading()
        productRepository.getMyProduct(token){ resultList, e ->
            hideLoading()
            e?.let { toast(it.message.toString()) }
            resultList?.let { products.postValue(it) }
        }
    }

    fun createProduct(token: String, productToSend : Product, imageUrl: String){
        try{
            setLoading()
            val map = HashMap<String, RequestBody>()
            map["name"] = createPartFromString(productToSend.name!!)
            map["price"] = createPartFromString(productToSend.price!!)
            map["description"] = createPartFromString(productToSend.description!!)
            map["address"] = createPartFromString(productToSend.address!!)
            val file = File(imageUrl)
            val requestBodyForFile = RequestBody.create(MediaType.parse("image/*"), file)
            val image = MultipartBody.Part.createFormData("image", file.name, requestBodyForFile)
            productRepository.createProduct(token, map, image){ resultBool, e ->
                hideLoading()
                e?.let { toast(it.message.toString()) }
                if(resultBool){ state.value = ProductState.Success }
            }
        }catch (e: Exception){
            println(e.message.toString())
            toast(e.message.toString())
        }

    }

    fun updateProduct(token: String, id : String, productToSend : Product, imageUrl: String){
        try{
            setLoading()
            val map = HashMap<String, RequestBody>()
            map["name"] = createPartFromString(productToSend.name!!)
            map["price"] = createPartFromString(productToSend.price!!)
            map["description"] = createPartFromString(productToSend.description!!)
            map["address"] = createPartFromString(productToSend.address!!)
            val file = File(imageUrl)
            val requestBodyForFile = RequestBody.create(MediaType.parse("image/*"), file)
            val image = MultipartBody.Part.createFormData("image", file.name, requestBodyForFile)
            productRepository.updateProduct(token, id, map, image){ resultBool, e ->
                hideLoading()
                e?.let { toast(it.message.toString()) }
                if(resultBool){ state.value = ProductState.Success }
            }
        }catch (e: Exception){
            println(e.message.toString())
            toast(e.message.toString())
        }

    }


    private fun createPartFromString(s: String) : RequestBody = RequestBody.create(MultipartBody.FORM, s)

    fun validate(name : String, price: String, address: String, desc: String, image: String?) : Boolean{
        state.value = ProductState.Reset
        if (name.isEmpty()){
            state.value = ProductState.Validate(name = "nama produk tidak boleh kosong")
            return false
        }
        if (price.isEmpty()){
            state.value = ProductState.Validate(price = "harga produk tidak boleh kosong")
            return false
        }
        if (address.isEmpty()){
            state.value = ProductState.Validate(address = "alamat produk tidak boleh kosong")
            return false
        }
        if (desc.isEmpty()){
            state.value = ProductState.Validate(desc = "deskripsi produk tidak boleh kosong")
            return false
        }
        if (image != null){
            if (image.isEmpty()){
                toast("foto produk tidak boleh kosong")
                return false
            }
        }
        return true
    }

    fun getState() = state
    fun getProducts() = products
}
sealed class ProductState {
    data class IsLoading(var state : Boolean = false) : ProductState()
    data class ShowToast(var message : String) : ProductState()
    object Success : ProductState()
    object Reset : ProductState()
    data class Validate(
        var name : String? = null,
        var price: String? = null,
        var address: String? = null,
        var desc: String? = null,
        var image: String? = null
    ) : ProductState()
}