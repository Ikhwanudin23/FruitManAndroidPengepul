package com.one.fruitmanpengepul.ui.register_activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.one.fruitmanpengepul.R
import com.one.fruitmanpengepul.utils.FruitmanUtil
import com.one.fruitmanpengepul.viewmodels.UserState
import com.one.fruitmanpengepul.viewmodels.UserViewModel
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    private lateinit var userViewModel : UserViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        supportActionBar?.hide()
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        userViewModel.listenToUIState().observer(this, Observer { handleUIState(it) })
        doRegister()
    }

    private fun handleUIState(it: UserState){
        when(it){
            is UserState.Success -> {
                FruitmanUtil.setToken(this, it.param)
                finish()
            }
            is UserState.IsLoading -> isLoading(it.state)
            is UserState.ShowAlert -> showAlert(it.message)
            is UserState.ShowToast -> toast(it.message)
            is UserState.Validate -> {
                it.name?.let { error -> setErrorName(error) }
                it.email?.let { error -> setErrorEmail(error) }
                it.password?.let { error -> setErrorPassword(error) }
            }
            is UserState.Reset -> {
                setErrorName(null)
                setErrorEmail(null)
                setErrorPassword(null)
            }
        }
    }

    private fun doRegister(){
        btn_register.setOnClickListener {
            val name = et_name.text.toString().trim()
            val email = et_email.text.toString().trim()
            val passwd = et_password.text.toString().trim()
            if(userViewModel.validateLogin(name, email, passwd)){
                userViewModel.register(name, email, passwd)
            }
        }
    }

    private fun isLoading(b : Boolean){
        loading.isIndeterminate = b
        btn_register.isEnabled = !b
    }

    private fun showAlert(message: String){
        AlertDialog.Builder(this).apply {
            setMessage(message)
            setPositiveButton(resources.getString(R.string.common_understand)){ d, _ -> d.dismiss() }
        }.show()
    }

    private fun setErrorName(e : String?) { in_name.error = e }
    private fun setErrorEmail(e : String?) { in_email.error = e }
    private fun setErrorPassword(e : String?) { in_password.error = e }
    private fun toast(message : String) = Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}
