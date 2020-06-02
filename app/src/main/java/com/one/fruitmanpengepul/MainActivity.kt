package com.one.fruitmanpengepul

import android.content.Intent
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.one.fruitmanpengepul.fragments.OrderFragment
import com.one.fruitmanpengepul.fragments.ProfileFragment
import com.one.fruitmanpengepul.fragments.TimelineFragment
import com.one.fruitmanpengepul.utils.FruitmanUtil
import com.one.fruitmanpengepul.viewmodels.OrderState
import com.one.fruitmanpengepul.viewmodels.OrderViewModel
import com.one.fruitmanpengepul.viewmodels.UserRole
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    companion object{ var navStatus = -1 }
    private var fragment : Fragment? = null
    private val orderViewModel: OrderViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        nav_view.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        supportActionBar?.hide()
        Thread(Runnable {
            if(FruitmanUtil.getToken(this@MainActivity) == null){
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                finish()
            }
        }).start()
        if(savedInstanceState == null){ nav_view.selectedItemId = R.id.navigation_home }
        orderViewModel.listenToRole().observe(this, Observer { listenToRoleSwitch(it) })
        orderViewModel.getState().observer(this, Observer { handleUIState(it) })
    }


    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                if(navStatus != 0){
                    fragment = TimelineFragment()
                    navStatus = 0
                }
            }
            R.id.navigation_order -> {
                if(navStatus != 1){
                    fragment = OrderFragment()
                    navStatus = 1
                }
            }
            R.id.navigation_profile -> {
                if(navStatus != 2){
                    fragment = ProfileFragment()
                    navStatus = 2
                }
            }
            else -> {
                fragment = TimelineFragment()
                navStatus = 0
            }
        }
        if(fragment == null){
            navStatus = 0
            fragment = TimelineFragment()
        }
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.screen_container, fragment!!)
        fragmentTransaction.commit()
        true
    }

    private fun listenToRoleSwitch(it: UserRole){
        FruitmanUtil.getToken(this@MainActivity)?.let { token ->
            val t = "Bearer $token"
            if(it == UserRole.BUYER){
                orderViewModel.collectorWaitingOrder(t)
            }else{
                orderViewModel.sellerGetOrderIn(t)
            }
        }
    }

    private fun handleUIState(it: OrderState){
        when(it){
            is OrderState.ShowToast -> toast(it.message)
            is OrderState.SuccessDelete -> {
                //fetch()
                toast(resources.getString(R.string.info_success_delete))
            }
            is OrderState.SuccessConfirmed -> {
                //fetch()
                toast(resources.getString(R.string.success_confirmed))
            }
            is OrderState.SuccessArrived -> {
                //fetch()
                toast(resources.getString(R.string.arrived))
            }
            is OrderState.SuccessCompleted -> {
                //fetch()
                toast("complete")
            }
        }
    }

    private fun fetch(){
        FruitmanUtil.getToken(this@MainActivity)?.let { token ->
            val t = "Bearer $token"
            val defaultValue = orderViewModel.listenToRole().value
            if(defaultValue == UserRole.BUYER){
                orderViewModel.collectorWaitingOrder(t)
            }else{
                orderViewModel.sellerGetOrderIn(t)
            }
        }
    }

    private fun toast(m: String) = Toast.makeText(this, m, Toast.LENGTH_LONG).show()

}
