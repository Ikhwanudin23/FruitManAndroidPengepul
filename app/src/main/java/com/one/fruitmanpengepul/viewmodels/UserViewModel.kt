package com.one.fruitmanpengepul.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.one.fruitmanpengepul.models.User
import com.one.fruitmanpengepul.repositories.UserRepository
import com.one.fruitmanpengepul.utils.FruitmanUtil
import com.one.fruitmanpengepul.utils.SingleLiveEvent
import com.one.fruitmanpengepul.webservices.ApiClient
import com.one.fruitmanpengepul.webservices.WrappedResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserViewModel(private val userRepository: UserRepository) : ViewModel(){
    private val api = ApiClient.instance()
    private var state : SingleLiveEvent<UserState> = SingleLiveEvent()
    private var user = MutableLiveData<User>()

    private fun setLoading() { state.value = UserState.IsLoading(true) }
    private fun hideLoading() { state.value = UserState.IsLoading(false) }
    private fun toast(message: String) { state.value = UserState.ShowToast(message) }
    private fun success(token: String) { state.value = UserState.Success(token) }

    fun validateLogin(name : String?, email: String, password: String) : Boolean{
        state.value = UserState.Reset
        if(name != null){
            if(name.isEmpty()){
                state.value = UserState.Validate(name = "Isi nama terlebih dahulu")
                return false
            }
        }

        if(!FruitmanUtil.isValidEmail(email)){
            state.value = UserState.Validate(email = "Email tidak valid")
            return false
        }
        if (!FruitmanUtil.isValidPassword(password)){
            state.value = UserState.Validate(password = "Password setidaknya delapan karakter")
            return false
        }
        return true
    }

    /*fun register(name : String, email: String, password: String){
        setLoading()
        api.register(name, email, password).enqueue(object: Callback<WrappedResponse<User>>{
            override fun onFailure(call: Call<WrappedResponse<User>>, t: Throwable) {
                println(t.message)
                state.value = UserState.ShowToast(t.message.toString())
                hideLoading()
            }

            override fun onResponse(call: Call<WrappedResponse<User>>, response: Response<WrappedResponse<User>>) {
                if(response.isSuccessful){
                    val b = response.body()
                    b?.let {
                        if(it.status){
                            state.value = UserState.Success(it.data?.token!!)
                        }else{
                            state.value = UserState.ShowAlert("Tidak dapat masuk. Periksa email dan kata sandi anda")
                        }
                    }
                }else{
                    state.value = UserState.ShowToast("Register gagal. Mungkin email ini sudah pernah didaftarkan")
                }
                hideLoading()
            }
        })
    }*/

    /*fun login(email: String, password: String){
        setLoading()
        api.login(email, password).enqueue(object : Callback<WrappedResponse<User>>{
            override fun onFailure(call: Call<WrappedResponse<User>>, t: Throwable) {
                println(t.message)
                hideLoading()
                state.value = UserState.ShowToast(t.message.toString())
            }

            override fun onResponse(call: Call<WrappedResponse<User>>, response: Response<WrappedResponse<User>>) {
                if(response.isSuccessful){
                    val b = response.body()
                    b?.let {
                        if(it.status){
                            state.value = UserState.Success(it.data?.token!!)
                        }else{
                            state.value = UserState.ShowAlert("Tidak dapat masuk. Periksa email dan kata sandi anda")
                        }
                    }
                }else{
                    state.value = UserState.ShowAlert("Tidak dapat masuk. Periksa email dan kata sandi anda")
                }
                hideLoading()
            }
        })
    }*/

    fun register(name: String, email: String, password: String){
        setLoading()
        userRepository.register(name, email, password){result, error->
            hideLoading()
            error?.let { toast(it.message.toString()) }
            result?.let { success(it) }
        }
    }

    fun login(email: String, password: String){
        setLoading()
        userRepository.login(email, password){result, error ->
            hideLoading()
            error?.let { toast(it.message.toString()) }
            result?.let { success(it) }
        }
    }

    fun profile(token : String){
        setLoading()
        userRepository.profile(token){result, error ->
            hideLoading()
            error?.let { toast(it.message.toString()) }
            result?.let { user.postValue(it) }
        }
    }

    fun listenToUIState() = state
    fun listenToUser() = user

}

sealed class UserState {
    object Reset : UserState()
    data class Validate(var name: String? = null, var email : String? = null, var password : String? = email) : UserState()
    data class IsLoading(var state : Boolean) : UserState()
    data class Success(var param : String) : UserState()
    data class ShowToast(var message : String) : UserState()
    data class ShowAlert(var message : String) : UserState()
}