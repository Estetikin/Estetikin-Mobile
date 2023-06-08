package com.codegeniuses.estetikin.ui.moduleDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.codegeniuses.estetikin.databinding.FragmentModulDetailBinding
import com.codegeniuses.estetikin.factory.ViewModelFactory
import com.codegeniuses.estetikin.helper.LoadingHandler
import com.codegeniuses.estetikin.model.result.Result

class ModulDetailFragment : Fragment(), LoadingHandler {

    private lateinit var binding: FragmentModulDetailBinding
    private lateinit var factory: ViewModelFactory
    private val moduleDetailViewModel: ModulDetailViewModel by viewModels { factory }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentModulDetailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
//        setupModule()
    }

    private fun setupViewModel() {
        factory = ViewModelFactory.getInstance(requireContext())
    }

    private fun setupModuleDetail() {
        moduleDetailViewModel.getAllModuleDetail().observe(requireActivity()) {
            it?.let { result ->
                when (result) {
                    is Result.Loading -> {
                        loadingHandler(true)
                    }
                    is Result.Error -> {
                        loadingHandler(false)
                        Toast.makeText(
                            requireContext(),
                            "Failed to fetch module data",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                    is Result.Success -> {
                        loadingHandler(false)
                        Toast.makeText(
                            requireContext(),
                            "Module fetched successfully!",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
            }
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

