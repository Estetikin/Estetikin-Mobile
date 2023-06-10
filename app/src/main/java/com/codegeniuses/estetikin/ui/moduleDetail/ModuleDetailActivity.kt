package com.codegeniuses.estetikin.ui.moduleDetail

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.codegeniuses.estetikin.databinding.ActivityModuleDetailBinding
import com.codegeniuses.estetikin.helper.LoadingHandler
import com.codegeniuses.estetikin.model.response.module.DataItem

class ModuleDetailActivity : AppCompatActivity(), LoadingHandler {

    private lateinit var binding: ActivityModuleDetailBinding
    private val adapter = ModuleDetailAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityModuleDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val layoutManager = LinearLayoutManager(this)
        binding.rvModule.layoutManager = layoutManager
        binding.rvModule.adapter = adapter

        setupView()

        binding.ivBackButton.setOnClickListener {
            supportFragmentManager.popBackStack()
        }
    }

    override fun onResume() {
        super.onResume()

        val layoutManager = LinearLayoutManager(this)
        binding.rvModule.layoutManager = layoutManager
        binding.rvModule.adapter = adapter

        setupView()
    }

    private fun setupView() {
        val module = intent.getParcelableExtra<DataItem>("module")
        if (module != null) {
            binding.apply {
                tvModuleTitlePlaceholde.text = module.title
                tvModuleDescriptionPlaceholder.text = module.description
            }
            adapter.setModuleDetailData(module.content)
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