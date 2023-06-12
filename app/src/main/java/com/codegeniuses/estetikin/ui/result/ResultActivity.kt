package com.codegeniuses.estetikin.ui.result

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.codegeniuses.estetikin.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
    }


    private fun setupView() {
        val intent = intent
        val fileUri = intent.getParcelableExtra<Uri>("image")
        if (fileUri != null) {
            binding.ivPicture.setImageURI(fileUri)
        }
    }


}