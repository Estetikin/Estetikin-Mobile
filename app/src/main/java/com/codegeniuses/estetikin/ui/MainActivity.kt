package com.codegeniuses.estetikin.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.codegeniuses.estetikin.R
import com.codegeniuses.estetikin.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val botNav: BottomNavigationView = binding.botNav

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_home_nav) as NavHostFragment
        val navController = navHostFragment.navController
        botNav.setupWithNavController(navController)

    }

    fun hideBottomNavigationView() {
        binding.botNav.visibility = View.GONE
    }


}