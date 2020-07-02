package com.one.fruitmanpengepul.ui.register_activity

import androidx.lifecycle.ViewModel
import com.one.fruitmanpengepul.repositories.UserRepository
import com.one.fruitmanpengepul.utils.FruitmanUtil
import com.one.fruitmanpengepul.utils.SingleLiveEvent

class RegisterViewModel (private val userRepository: UserRepository) : ViewModel(){

    private val state : SingleLiveEvent<RegisterState> = SingleLiveEvent()
    private fun toast(message: String) { state.value = RegisterState.ShowToast(message) }
    private fun setLoading() { state.value = RegisterState.IsLoading(true) }
    private fun hideLoading() { state.value = RegisterState.IsLoading(false) }
    private fun successRegister() { state.value = RegisterState.SuccessRegister }

    fun register(name : String, email : String, password : String){
        setLoading()
        userRepository.register(name, email, password){registerBool, error->
            hideLoading()
            error?.let { it.message?.let { message-> toast(message) } }
            if (registerBool){
                successRegister()
            }
        }
    }

    fun validate(name: String, email: String, password: String, confirmPassword : String): Boolean{
        state.value = RegisterState.Reset
        if (name.isEmpty()){
            state.value = RegisterState.ShowToast("nama tidak boleh kosong")
            return false
        }
        if (name.length < 5){
            state.value = RegisterState.ShowToast("nama setidaknya 5 karakter")
            return false
        }

        if (email.isEmpty() || password.isEmpty()){
            state.value = RegisterState.ShowToast("mohon isi semua form")
            return false
        }
        if (!FruitmanUtil.isValidEmail(email)){
            state.value = RegisterState.Validate(email = "email tidak valid")
            return false
        }
        if (!FruitmanUtil.isValidPassword(password)){
            state.value = RegisterState.Validate(password = "password tidak valid")
            return false
        }
        if (confirmPassword.isEmpty()){
            state.value = RegisterState.ShowToast("Isi semua form terlebih dahulu")
            return false
        }
        if(!confirmPassword.equals(password)){
            state.value = RegisterState.Validate(confirmPassword = "Konfirmasi password tidak cocok")
            return false
        }
        return true
    }

    fun listenToState() = state
}

sealed class RegisterState{
    data class IsLoading(var state : Boolean = false) : RegisterState()
    data class ShowToast(var message : String) : RegisterState()
    object SuccessRegister : RegisterState()
    data class Validate(
        var name : String? = null,
        var email : String? = null,
        var password : String? = null,
        var confirmPassword : String? = null,
        var telp : String? = null
    ) : RegisterState()
    object Reset : RegisterState()
}