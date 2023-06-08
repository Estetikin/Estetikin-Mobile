package com.codegeniuses.estetikin.ui.sentiment

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.codegeniuses.estetikin.databinding.ActivitySentimentBinding
import com.codegeniuses.estetikin.ui.MainActivity

class SentimentActivity : AppCompatActivity() {
    private var _binding: ActivitySentimentBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnIos.setOnClickListener{
//            TODO(Call the user pref fun here)
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.btnAndroid.setOnClickListener {
//            TODO(Call the user pref fun here)
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.btnDslr.setOnClickListener {
//            TODO(Call the user pref fun here)
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.btnVideo.setOnClickListener {
//            TODO(Call the user pref fun here)
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}