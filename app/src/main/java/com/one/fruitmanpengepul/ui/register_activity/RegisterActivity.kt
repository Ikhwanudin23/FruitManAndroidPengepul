package com.one.fruitmanpengepul.ui.register_activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.one.fruitmanpengepul.R
import com.one.fruitmanpengepul.ui.login_activity.LoginActivity
import com.one.fruitmanpengepul.utils.FruitmanUtil
import com.one.fruitmanpengepul.viewmodels.UserState
import com.one.fruitmanpengepul.viewmodels.UserViewModel
import kotlinx.android.synthetic.main.activity_register.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterActivity : AppCompatActivity() {

    private val registerViewModel : RegisterViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        supportActionBar?.hide()
        registerViewModel.listenToState().observer(this, Observer { handleUIState(it) })
        doRegister()
    }

    private fun handleUIState(it: RegisterState){
        when(it){
            is RegisterState.SuccessRegister -> {
                showAlert("silahkan cek email anda")
            }
            is RegisterState.IsLoading -> isLoading(it.state)
            is RegisterState.ShowToast -> toast(it.message)
            is RegisterState.Validate -> {
                it.name?.let { error -> setErrorName(error) }
                it.email?.let { error -> setErrorEmail(error) }
                it.password?.let { error -> setErrorPassword(error) }
                it.confirmPassword?.let { error -> setErrorConfrimPassword(error) }
            }
            is RegisterState.Reset -> {
                setErrorName(null)
                setErrorEmail(null)
                setErrorPassword(null)
                setErrorConfrimPassword(null)
            }
        }
    }

    private fun doRegister(){
        btn_register.setOnClickListener {
            val name = et_name.text.toString().trim()
            val email = et_email.text.toString().trim()
            val passwd = et_password.text.toString().trim()
            val confirm_pass = et_confirm_password.text.toString().trim()
            if(registerViewModel.validate(name, email, passwd, confirm_pass)){
                registerViewModel.register(name, email, passwd)
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
            setPositiveButton(resources.getString(R.string.common_understand)){ d, _ ->
                d.dismiss()
                startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
            }
        }.show()
    }

    private fun setErrorName(e : String?) { in_name.error = e }
    private fun setErrorEmail(e : String?) { in_email.error = e }
    private fun setErrorPassword(e : String?) { in_password.error = e }
    private fun setErrorConfrimPassword(e : String?) { in_confirm_password.error = e }
    private fun toast(message : String) = Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}
