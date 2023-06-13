package com.codegeniuses.estetikin.ui.result

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.codegeniuses.estetikin.databinding.ActivityResultBinding
import com.codegeniuses.estetikin.ui.MainActivity
import com.codegeniuses.estetikin.ui.album.AlbumFragment
import com.codegeniuses.estetikin.ui.home.HomeFragment

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        setupView()
    }

    private fun setupView() {
        val intent = intent
        val fileUri = intent.getParcelableExtra<Uri>("image")
        if (fileUri != null) {
            binding.ivPicture.setImageURI(fileUri)
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags =
            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }


}