package com.one.fruitmanpengepul.ui.login_activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import com.one.fruitmanpengepul.R
import com.one.fruitmanpengepul.ui.register_activity.RegisterActivity
import com.one.fruitmanpengepul.ui.main_activity.MainActivity
import com.one.fruitmanpengepul.utils.FruitmanUtil
import com.one.fruitmanpengepul.viewmodels.UserState
import com.one.fruitmanpengepul.viewmodels.UserViewModel
import kotlinx.android.synthetic.main.activity_login.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity() {
    private val userViewModel : UserViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()
        goToRegister()
        doLogin()
        userViewModel.listenToUIState().observer(this, Observer { handleUIState(it) })
    }

    private fun handleUIState(it : UserState){
        when(it){
            is UserState.IsLoading -> {
                loading.isIndeterminate = it.state
                btn_login.isEnabled = !it.state
                btn_goto_register.isEnabled = !it.state
            }
            is UserState.Reset -> {
                setErrorEmail(null)
                setErrorPassword(null)
            }
            is UserState.Validate -> {
                it.email?.let { e -> setErrorEmail(e) }
                it.password?.let { e -> setErrorPassword(e) }
            }
            is UserState.ShowToast -> toast(it.message)
            is UserState.ShowAlert -> showAlert(it.message)
            is UserState.Success -> {
                FruitmanUtil.setToken(this@LoginActivity, it.param)
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                finish()
            }
        }
    }

    private fun doLogin(){
        btn_login.setOnClickListener {
            val e = et_email.text.toString().trim()
            val p = et_password.text.toString().trim()
            if(userViewModel.validateLogin(null, e, p)){
                userViewModel.login(e, p)
            }
        }
    }

    private fun goToRegister() = btn_goto_register.setOnClickListener { startActivity(Intent(this@LoginActivity, RegisterActivity::class.java)) }

    private fun showAlert(m : String){
        AlertDialog.Builder(this).apply {
            setMessage(m)
            setPositiveButton(resources.getString(R.string.common_understand)){ d, w ->
                d.dismiss()
            }
        }.show()
    }

    override fun onResume() {
        super.onResume()
        if(FruitmanUtil.getToken(this) != null){
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            finish()
        }
    }


    private fun setErrorEmail(e : String?) { in_email.error = e}
    private fun setErrorPassword(e : String?) { in_password.error = e }
    private fun toast(message : String) = Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}
