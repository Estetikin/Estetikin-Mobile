package com.codegeniuses.estetikin.ui.forgetPassword

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.codegeniuses.estetikin.databinding.ActivityForgetPasswordBinding
import com.codegeniuses.estetikin.factory.ViewModelFactory
import com.codegeniuses.estetikin.helper.LoadingHandler
import com.codegeniuses.estetikin.model.result.Result
import com.codegeniuses.estetikin.ui.authentication.AuthActivity

class ForgetPasswordActivity : AppCompatActivity(), LoadingHandler {
    private lateinit var binding: ActivityForgetPasswordBinding
    private lateinit var factory: ViewModelFactory
    private val viewModel: ForgetPasswordViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        setupViewModel()
        setupAction()
        navigateToLogin()
    }

    private fun setupAction() {
        binding.btnResetPassword.setOnClickListener {
            val email = binding.etEmail.text.toString()
            viewModel.sendEmail(email).observe(this) { result ->
                when (result) {
                    is Result.Loading -> {
                        loadingHandler(true)
                    }
                    is Result.Error -> {
                        loadingHandler(false)
                        Toast.makeText(
                            this,
                            "Failed to send email",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    is Result.Success -> {
                        loadingHandler(false)
                        navigateToCheckEmail()
                    }
                }

            }
        }
    }

    private fun setupViewModel() {
        factory = ViewModelFactory.getInstance(binding.root.context)
    }

    private fun navigateToCheckEmail() {
        val intent = Intent(this@ForgetPasswordActivity, CheckEmailActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToLogin() {
        binding.tvBackToLogin.setOnClickListener {
            val intent = Intent(this@ForgetPasswordActivity, AuthActivity::class.java)
            startActivity(intent)
        }
    }


    override fun loadingHandler(isLoading: Boolean) {
        if (isLoading) {
            binding.loadingAnimation.visibility = View.VISIBLE
        } else {
            binding.loadingAnimation.visibility = View.GONE
        }
    }
}