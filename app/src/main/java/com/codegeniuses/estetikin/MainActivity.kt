package com.codegeniuses.estetikin

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.codegeniuses.estetikin.databinding.ActivityMainBinding
import com.codegeniuses.estetikin.ui.login.LoginActivity
import com.codegeniuses.estetikin.ui.signup.SignUpActivity
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        binding.botNav.background = null
        binding.botNav.menu.getItem(2).isEnabled = false


        handleNavigation()
        handleTest()

    }

    private fun handleTest() {
        binding.btnTestLogin.setOnClickListener{
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.btnTestSignUp.setOnClickListener{
            val intent = Intent(this@MainActivity, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    private fun handleNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_home_nav) as NavHostFragment
        val navController = navHostFragment.navController

        val botNav: BottomNavigationView = binding.botNav
        botNav.setupWithNavController(navController)
    }
}