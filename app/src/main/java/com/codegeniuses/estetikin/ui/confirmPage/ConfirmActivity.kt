package com.codegeniuses.estetikin.ui.confirmPage

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.codegeniuses.estetikin.databinding.ActivityConfirmBinding
import com.codegeniuses.estetikin.ui.result.ResultActivity
import com.codegeniuses.estetikin.ui.sentiment.SentimentActivity

class ConfirmActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConfirmBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfirmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent
        val fileUri = intent.getParcelableExtra<Uri>("image")

        setupView(fileUri)
        setupAction(fileUri)
    }

    private fun setupView(fileUri: Uri?) {
        if (fileUri != null) {
            binding.ivYourImage.setImageURI(fileUri)
        }
    }

    private fun setupAction(fileUri: Uri?) {
        binding.btnSend.setOnClickListener {
            classifyImage(fileUri)
            moveToResultActivity(fileUri)
        }
    }

    private fun classifyImage(fileUri: Uri?) {
        Toast.makeText(this, "proses ml", Toast.LENGTH_SHORT).show()
    }

    private fun moveToResultActivity(fileUri: Uri?){
        val intent = Intent(this@ConfirmActivity, ResultActivity::class.java)
        intent.putExtra("image", fileUri)
        startActivity(intent)
    }

}


