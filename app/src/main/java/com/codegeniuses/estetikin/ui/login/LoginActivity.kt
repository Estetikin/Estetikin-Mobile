package com.codegeniuses.estetikin.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.codegeniuses.estetikin.R
import com.codegeniuses.estetikin.data.local.UserPreference
import com.codegeniuses.estetikin.databinding.ActivityLoginBinding
import com.codegeniuses.estetikin.factory.ViewModelFactory
import com.codegeniuses.estetikin.helper.LoadingHandler
import com.codegeniuses.estetikin.model.response.LoginResponse
import com.codegeniuses.estetikin.model.result.Result.*
import com.codegeniuses.estetikin.ui.MainActivity
import com.codegeniuses.estetikin.ui.home.HomeFragment
import com.codegeniuses.estetikin.ui.signup.SignUpActivity


class LoginActivity : AppCompatActivity(), LoadingHandler {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var factory: ViewModelFactory
    private val loginViewModel: LoginViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

//        checkToken()
        setupViewModel()
        playAnimation()
        setupAction()
    }

    fun setupViewModel() {
        factory = ViewModelFactory.getInstance(binding.root.context)
    }

    private fun playAnimation() {

        val image =
            ObjectAnimator.ofFloat(binding.ivLoginIllustration, View.ALPHA, 1f).setDuration(500)
        val title = ObjectAnimator.ofFloat(binding.tvLogin, View.ALPHA, 1f).setDuration(500)
        val loginMessage =
            ObjectAnimator.ofFloat(binding.tvLoginMsg, View.ALPHA, 1f).setDuration(500)
        val username = ObjectAnimator.ofFloat(binding.etEmail, View.ALPHA, 1f).setDuration(500)
        val password = ObjectAnimator.ofFloat(binding.etPassword, View.ALPHA, 1f).setDuration(500)
        val forgotPassword =
            ObjectAnimator.ofFloat(binding.tvForgotPassword, View.ALPHA, 1f).setDuration(500)
        val loginButton = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(500)
        val loginWith = ObjectAnimator.ofFloat(binding.llLoginWith, View.ALPHA, 1f).setDuration(500)
        val googleLogo =
            ObjectAnimator.ofFloat(binding.llGoogleLogo, View.ALPHA, 1f).setDuration(500)
        val btnRegister =
            ObjectAnimator.ofFloat(binding.btnSignUp, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(
                image,
                title,
                loginMessage,
                username,
                password,
                forgotPassword,
                loginButton,
                loginWith,
                googleLogo,
                btnRegister
            )
            startDelay = 500
        }.start()

        binding.ivLoginIllustration.animate().apply {
            duration = 1000
            alpha(.5f)
            rotationYBy(360f)
            translationYBy(200f)
        }.withEndAction{
            binding.ivLoginIllustration.animate().apply {
                duration = 1000
                alpha(1f)
                rotationXBy(360f)
                translationYBy(-200f)
            }
        }.start()

        ObjectAnimator.ofFloat(binding.ivLoginIllustration, View.TRANSLATION_X, -50f, 50f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
        ObjectAnimator.ofFloat(binding.tvLogin, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
        ObjectAnimator.ofFloat(binding.tvLoginMsg, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
    }

    private fun setupAction() {
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            loginViewModel.login(email, password).observe(this@LoginActivity) { result ->
                if (result != null) {
                    when (result) {
                        is Loading -> {
                            loadingHandler(true)
                        }
                        is Error -> {
                            loadingHandler(false)
                            Toast.makeText(this, "Failed to Login", Toast.LENGTH_SHORT).show()
                        }
                        is Success -> {
                            loadingHandler(false)
                            Toast.makeText(this, "Login Success!", Toast.LENGTH_SHORT).show()
                            saveTokenToPreference(result.data)
                            navigateToHomeFragment()
                        }
                    }
                }

            }


        }

        binding.btnSignUp.setOnClickListener {
            val intent = Intent(this@LoginActivity, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    private fun navigateToHomeFragment() {
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        startActivity(intent)
    }


    private fun saveTokenToPreference(data: LoginResponse) {
        val pref = UserPreference(this)
        val result = data.token
        pref.saveToken(result)
    }

    private fun checkToken() {
        val pref = UserPreference(this)
        val token = pref.getToken()
        if (!token.isNullOrEmpty()) {
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            intent.flags =
                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
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