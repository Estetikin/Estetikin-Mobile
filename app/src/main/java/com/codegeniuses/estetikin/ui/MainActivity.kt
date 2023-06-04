package com.codegeniuses.estetikin.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.codegeniuses.estetikin.R
import com.codegeniuses.estetikin.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        binding.botNav.background = null
        binding.botNav.menu.getItem(2).isEnabled = false


        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_home_nav) as NavHostFragment
        val navController = navHostFragment.navController

        val botNav: BottomNavigationView = binding.botNav
        botNav.setupWithNavController(navController)

//        navController.addOnDestinationChangedListener { _, nd: NavDestination, _ ->
//            if (nd.id == R.id.onBoardingActivity) {
//                binding.bottom.visibility = View.GONE
//            } else {
//                binding.bottom.visibility = View.VISIBLE
//            }
//        }

    }

    fun showBottomNavigationView() {
        binding.bottom.visibility = View.VISIBLE
    }

}

