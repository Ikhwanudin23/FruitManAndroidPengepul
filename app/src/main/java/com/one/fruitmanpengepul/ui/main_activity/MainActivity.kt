package com.one.fruitmanpengepul.ui.main_activity

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.one.fruitmanpengepul.ui.login_activity.LoginActivity
import com.one.fruitmanpengepul.R
import com.one.fruitmanpengepul.fragments.OrderFragment
import com.one.fruitmanpengepul.fragments.ProfileFragment
import com.one.fruitmanpengepul.fragments.TimelineFragment
import com.one.fruitmanpengepul.utils.FruitmanUtil
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    companion object{ var navStatus = -1 }
    private var fragment : Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_main)
        nav_view.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        supportActionBar?.hide()
        Thread(Runnable {
            if(FruitmanUtil.getToken(this@MainActivity) == null){
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                finish()
            }
        }).start()
        if(savedInstanceState == null){ nav_view.selectedItemId =
            R.id.navigation_home
        }
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


}
