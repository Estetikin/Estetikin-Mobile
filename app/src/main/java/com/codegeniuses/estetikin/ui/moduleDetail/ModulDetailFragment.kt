package com.codegeniuses.estetikin.ui.moduleDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.codegeniuses.estetikin.R
import com.codegeniuses.estetikin.databinding.FragmentModulDetailBinding
import com.codegeniuses.estetikin.helper.LoadingHandler
import com.codegeniuses.estetikin.model.response.module.DataItem
import com.codegeniuses.estetikin.ui.MainActivity

class ModulDetailFragment : Fragment(), LoadingHandler {

    private var _binding: FragmentModulDetailBinding? = null
    private val binding get() = _binding!!
    private val adapter = ModuleDetailAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentModulDetailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvModule.layoutManager = layoutManager
        binding.rvModule.adapter = adapter

        setupView()
    }

    override fun onResume() {
        super.onResume()
        (activity as? MainActivity)?.setActionBarTitle(getString(R.string.title_module_detail))
        val bottomNavigation: CoordinatorLayout = requireActivity().findViewById(R.id.bottom)
        bottomNavigation.visibility = View.INVISIBLE

        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvModule.layoutManager = layoutManager
        binding.rvModule.adapter = adapter

        setupView()
    }

    private fun setupView() {
        val module = arguments?.getParcelable<DataItem>("module") ?: null
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

