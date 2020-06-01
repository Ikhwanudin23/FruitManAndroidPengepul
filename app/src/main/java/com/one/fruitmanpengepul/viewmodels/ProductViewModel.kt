package com.one.fruitmanpengepul.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.one.fruitmanpengepul.models.Product
import com.one.fruitmanpengepul.utils.SingleLiveEvent
import com.one.fruitmanpengepul.webservices.ApiClient
import com.one.fruitmanpengepul.webservices.WrappedListResponse
import com.one.fruitmanpengepul.webservices.WrappedResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductViewModel : ViewModel(){
    private var products = MutableLiveData<List<Product>>()
    private var state : SingleLiveEvent<ProductState> = SingleLiveEvent()
    private val api = ApiClient.instance()

    private fun setLoading() { state.value = ProductState.IsLoading(true) }
    private fun hideLoading() { state.value = ProductState.IsLoading(false) }
    private fun toast(message: String) {state.value = ProductState.ShowToast(message)}
    private fun success() { state.value = ProductState.Success}

    fun getAllProduct(token : String){
        setLoading()
        api.getAllProduct(token).enqueue(object : Callback<WrappedListResponse<Product>>{
            override fun onFailure(call: Call<WrappedListResponse<Product>>, t: Throwable) {
                println("OnFailure : ${t.message}")
                hideLoading()
            }

            override fun onResponse(call: Call<WrappedListResponse<Product>>, response: Response<WrappedListResponse<Product>>) {
                if (response.isSuccessful){
                    val body = response.body()
                    if (body?.status!!){
                        val data = body.data
                        products.postValue(data)
                    }else{
                        toast("status false ${body.message}")
                    }
                }else{
                    toast("not response ${response.message()}")
                }
                hideLoading()
            }

        })
    }

    fun getMyProduct(token: String){
        setLoading()
        api.getMyProduct(token).enqueue(object : Callback<WrappedListResponse<Product>>{
            override fun onFailure(call: Call<WrappedListResponse<Product>>, t: Throwable) {
                hideLoading()
                toast("OnFailure : ${t.message}")
                println("OnFailure : ${t.message}")
            }

            override fun onResponse(call: Call<WrappedListResponse<Product>>, response: Response<WrappedListResponse<Product>>) {
                if (response.isSuccessful){
                    val body  = response.body()
                    if (body?.status!!){
                        val data = body.data
                        products.postValue(data)
                    }else{
                        toast("status false ${body.message}")
                        println("status false ${body.message}")
                    }
                }else{
                    toast("not response ${response.message()}")
                    println("not response ${response.message()}")
                }
                hideLoading()
            }

        })
    }

    fun postProduct(token : String, name : String, price : String, address : String, desc : String, image : String){
        setLoading()
        api.postProduct(token, name, price.toInt(), address, desc, image).enqueue(object : Callback<WrappedResponse<Product>>{
            override fun onFailure(call: Call<WrappedResponse<Product>>, t: Throwable) {
                println("OnFailure : ${t.message}")
                toast("OnFailure : ${t.message}")
                hideLoading()
            }

            override fun onResponse(call: Call<WrappedResponse<Product>>, response: Response<WrappedResponse<Product>>) {
                if (response.isSuccessful){
                    val body = response.body()
                    if (body?.status!!){
                        success()
                        toast("berhasil menambahkan produk ${name}")
                    }else{
                        toast("status false ${body.message}")
                        println("status false ${body.message}")
                    }
                }else{
                    toast("no response ${response.message()}")
                    println("no response ${response.message()}")
                }
                hideLoading()
            }

        })
    }

    fun validate(name : String, price: String, address: String, desc: String, image: String?) : Boolean{
        state.value = ProductState.Reset
        if (name.isEmpty()){
            toast("nama produk tidak boleh kosong")
            return false
        }
        if (price.isEmpty()){
            toast("harga produk tidak boleh kosong")
            return false
        }
        if (address.isEmpty()){
            toast("alamat produk tidak boleh kosong")
            return false
        }
        if (desc.isEmpty()){
            toast("deskripsi produk tidak boleh kosong")
            return false
        }
//        if (image.isEmpty()){
//            toast("foto produk tidak boleh kosong")
//            return false
//        }
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