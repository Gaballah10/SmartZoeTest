package com.example.smartzonetest.app.activity

import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import androidx.fragment.app.Fragment
import com.example.smartzonetest.R
import com.example.smartzonetest.app.fragment.FavoritesFragment
import com.example.smartzonetest.app.fragment.HomeFragment
import com.example.smartzonetest.app.util.addWithAnimation
import com.example.smartzonetest.app.util.debug
import com.example.smartzonetest.app.util.remove
import com.example.smartzonetest.network.NetworkState
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), NetworkState.NetworkStateReceiverListener {
    lateinit var bottomNavigationView: BottomNavigationView
    private var networkState: NetworkState? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        networkState = NetworkState()
        networkState!!.addListener(this)
        handlefragment(HomeFragment())
        bottomfun()
    }

    private fun bottomfun() {
        bottomNavigationView = findViewById(R.id.bottom_navigation_main) as BottomNavigationView
        bottomNavigationView.itemIconTintList = null
        val menu: Menu = bottomNavigationView.menu
        bottomNavigationView.setSelectedItemId(R.id.my_home)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.getItemId()) {
                R.id.my_home -> {
                    view_tool.visibility = View.GONE
                    handlefragment(HomeFragment())
                }
                R.id.favorite -> {
                    toolbar_home.visibility = View.GONE
                    handlefragment(FavoritesFragment())
                }
            }
            true
        }
    }

    private fun handlefragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.home_page_frame, fragment)
            .commit()
    }

    override fun onPause() {
        super.onPause()
        try {
            unregisterReceiver(networkState)
        } catch (e: Exception) {
            Log.e("AllDebug:NetworkState", "Error:${e.message}")
        }
    }

    override fun networkAvailable() {
        no_internet_message?.remove()
        debug("network available")
    }

    override fun networkUnavailable() {
        no_internet_message?.addWithAnimation()
        debug("network not available")
    }

    override fun onStart() {
        super.onStart()
        registerReceiver(networkState, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }


}