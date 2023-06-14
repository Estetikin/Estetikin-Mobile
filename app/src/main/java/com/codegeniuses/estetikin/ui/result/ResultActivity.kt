package com.codegeniuses.estetikin.ui.result

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.codegeniuses.estetikin.R
import com.codegeniuses.estetikin.databinding.ActivityResultBinding
import com.codegeniuses.estetikin.ui.MainActivity
import com.codegeniuses.estetikin.ui.example.ExampleActivity

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

        val class1 = intent.getIntExtra("model1", 0)
        val class2 = intent.getIntExtra("model2", 0)
        val class3 = intent.getIntExtra("model3", 0)
        val class4 = intent.getIntExtra("model4", 0)

        if (fileUri != null) {
            binding.ivPicture.setImageURI(fileUri)
        }

        classifyText(class1, class2, class3, class4)
        binding.btnExample.setOnClickListener {
            navigateToExampleResult(fileUri,class1, class2, class3, class4)
        }

    }

    private fun classifyText(model1: Int, model2: Int, model3: Int, model4: Int) {
        when (model2) {
            0 -> binding.tvModule1.setText(R.string.food_beverage)
            1 -> binding.tvModule1.setText(R.string.indoor)
            2 -> binding.tvModule1.setText(R.string.outdoor)
            3 -> binding.tvModule1.setText(R.string.model)
        }

        when (model1) {
            0 -> binding.tvModule2.setText(R.string.wellFocused)
            1 -> binding.tvModule2.setText(R.string.blurry)
        }
        when (model3) {
            0 -> binding.tvModule3.setText(R.string.normal)
            1 -> binding.tvModule3.setText(R.string.dark)
            2 -> binding.tvModule3.setText(R.string.bright)
        }
        when (model4) {
            0 -> binding.tvModule4.setText(R.string.centered)
            1 -> binding.tvModule4.setText(R.string.nonCentered)
        }


    }

    private fun navigateToExampleResult(fileUri:Uri?,class1: Int, class2: Int, class3: Int, class4: Int) {
        val intent = Intent(this, ExampleActivity::class.java)
        intent.putExtra("image", fileUri)
        intent.putExtra("model1", class1)
        intent.putExtra("model2", class2)
        intent.putExtra("model3", class3)
        intent.putExtra("model4", class4)
        startActivity(intent)
    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags =
            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }


}