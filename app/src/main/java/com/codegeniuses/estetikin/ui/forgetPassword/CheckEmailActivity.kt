package com.codegeniuses.estetikin.ui.forgetPassword

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.codegeniuses.estetikin.databinding.ActivityCheckEmailBinding
import com.codegeniuses.estetikin.ui.authentication.AuthActivity

class CheckEmailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCheckEmailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckEmailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        navigateToLogin()
    }

    private fun navigateToLogin() {
        binding.btnOkCheckEmail.setOnClickListener {
            val intent = Intent(this@CheckEmailActivity, AuthActivity::class.java)
            startActivity(intent)
        }
    }
}