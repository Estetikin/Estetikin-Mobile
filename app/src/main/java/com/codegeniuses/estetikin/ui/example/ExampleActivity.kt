package com.codegeniuses.estetikin.ui.example

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.codegeniuses.estetikin.R
import com.codegeniuses.estetikin.databinding.ActivityExampleBinding
import com.codegeniuses.estetikin.ui.result.ResultActivity

class ExampleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityExampleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExampleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        setupView()

    }

    private fun setupView() {
        val intent = intent
        val class1 = intent.getIntExtra("model1", 0)
        val class2 = intent.getIntExtra("model2", 0)
        val class3 = intent.getIntExtra("model3", 0)
        val class4 = intent.getIntExtra("model4", 0)
        bindingImage(class1, class2, class3, class4)
    }

    private fun bindingImage(model1: Int, model2: Int, model3: Int, model4: Int) {
        when (model2) {
            0 -> binding.ivModel1.setImageResource(R.drawable.model1_4)
            1 -> binding.ivModel1.setImageResource(R.drawable.model1_2)
            2 -> binding.ivModel1.setImageResource(R.drawable.model1_3)
            3 -> binding.ivModel1.setImageResource(R.drawable.model1_1)
        }
        when (model1) {
            0 -> binding.ivModel2.setImageResource(R.drawable.model2_2)
            1 -> binding.ivModel2.setImageResource(R.drawable.model2_1)
        }
        when (model3) {
            0 -> binding.ivModel3.setImageResource(R.drawable.model3_3)
            1 -> binding.ivModel3.setImageResource(R.drawable.model3_2)
            2 -> binding.ivModel3.setImageResource(R.drawable.model3_1)
        }
        when (model4) {
            0 -> binding.ivModel4.setImageResource(R.drawable.modul4_2)
            1 -> binding.ivModel4.setImageResource(R.drawable.modul4_1)
        }
    }

    override fun onBackPressed() {
        finish()
    }


}