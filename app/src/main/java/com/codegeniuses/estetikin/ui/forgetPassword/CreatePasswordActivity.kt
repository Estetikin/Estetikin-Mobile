package com.codegeniuses.estetikin.ui.forgetPassword

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.codegeniuses.estetikin.databinding.ActivityCreatePasswordBinding
import com.codegeniuses.estetikin.ui.authentication.AuthActivity

class CreatePasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreatePasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreatePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        navigateToLogin()
    }

    private fun navigateToLogin(){
        binding.btnSavePassword.setOnClickListener {
            val intent = Intent (this@CreatePasswordActivity, AuthActivity::class.java)
            startActivity(intent)
        }
    }
}