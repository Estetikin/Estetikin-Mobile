package com.codegeniuses.estetikin.ui.sentiment

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.codegeniuses.estetikin.data.local.UserPreference
import com.codegeniuses.estetikin.databinding.ActivitySentimentBinding
import com.codegeniuses.estetikin.ui.MainActivity

class SentimentActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySentimentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySentimentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAction()
    }

    private fun setupAction() {
        binding.btnIos.setOnClickListener {
            saveUserPreference("ios")
            makeText("ios")
            navigateToHomeFragment()
        }

        binding.btnAndroid.setOnClickListener {
            saveUserPreference("android")
            makeText("android")
            navigateToHomeFragment()
        }

        binding.btnDslr.setOnClickListener {
            saveUserPreference("dslr")
            makeText("dslr")
            navigateToHomeFragment()
        }

        binding.btnVideo.setOnClickListener {
            saveUserPreference("video")
            makeText("video")
            navigateToHomeFragment()
        }
    }

    private fun makeText(text: String) {
        Toast.makeText(this@SentimentActivity, "You choose $text as your preference", Toast.LENGTH_SHORT).show()
    }

    private fun navigateToHomeFragment() {
        val intent = Intent(this@SentimentActivity, MainActivity::class.java)
        intent.flags =
            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    private fun saveUserPreference(data: String) {
        val pref = UserPreference(this)
        pref.saveUserPreference(data)
    }

}