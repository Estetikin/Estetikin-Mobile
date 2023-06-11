package com.codegeniuses.estetikin.ui.forgetPassword

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.codegeniuses.estetikin.databinding.ActivityForgetPasswordBinding
import com.codegeniuses.estetikin.ui.authentication.AuthActivity

class ForgetPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityForgetPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        setupAction()
        navigateToLogin()
    }

    private fun setupAction(){
        binding.btnResetPassword.setOnClickListener {
            val intent = Intent(this@ForgetPasswordActivity, CheckEmailActivity::class.java)
            startActivity(intent)
        }
    }

    private fun navigateToLogin(){
        binding.tvBackToLogin.setOnClickListener{
            val intent = Intent(this@ForgetPasswordActivity, AuthActivity::class.java)
            startActivity(intent)
        }
    }
}