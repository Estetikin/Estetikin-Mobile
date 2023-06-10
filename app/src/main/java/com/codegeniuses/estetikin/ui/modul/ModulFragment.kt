package com.codegeniuses.estetikin.ui.modul

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.codegeniuses.estetikin.R
import com.codegeniuses.estetikin.databinding.FragmentModulBinding
import com.codegeniuses.estetikin.factory.ViewModelFactory
import com.codegeniuses.estetikin.helper.LoadingHandler
import com.codegeniuses.estetikin.model.response.album.ArrAlbumItem
import com.codegeniuses.estetikin.model.response.module.DataItem
import com.codegeniuses.estetikin.model.result.Result
import com.codegeniuses.estetikin.ui.MainActivity
import com.codegeniuses.estetikin.ui.album.AlbumAdapter
import com.codegeniuses.estetikin.ui.albumDetail.AlbumDetailFragment
import com.codegeniuses.estetikin.ui.article.ArticleAdapter
import com.codegeniuses.estetikin.ui.moduleDetail.ModulDetailFragment

class ModulFragment : Fragment(), LoadingHandler {
    private var _binding: FragmentModulBinding? = null
    private val binding get() = _binding!!
    private lateinit var factory: ViewModelFactory
    private val moduleViewModel: ModuleViewModel by viewModels { factory }
    private val adapter = ModuleAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentModulBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvItemModule.layoutManager = layoutManager
        binding.rvItemModule.adapter = adapter

    }
    override fun onResume() {
        super.onResume()
        (activity as? MainActivity)?.setActionBarTitle(getString(R.string.title_module))
        val bottomNavigation: CoordinatorLayout = requireActivity().findViewById(R.id.bottom)
        bottomNavigation.visibility = View.VISIBLE

        setupViewModel()
        setupModule()
        setupAction()
    }

    private fun setupModule() {
        moduleViewModel.getAllModule().observe(requireActivity()) {
            it?.let { result ->
                when (result) {
                    is Result.Loading -> {
                        loadingHandler(true)
                    }
                    is Result.Error -> {
                        loadingHandler(false)
                        Toast.makeText(
                            requireContext(),
                            "Failed to fetch module",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                    is Result.Success -> {
                        loadingHandler(false)
                        adapter.setModuleData(result.data.data)
                    }
                }
            }
        }
    }

    private fun setupAction() {
        adapter.setOnItemClickCallback(object : ModuleAdapter.OnItemClickCallBack {
            override fun onItemClicked(data: DataItem) {
                showSelectedModule(data)
            }
        })
    }

    private fun showSelectedModule(data: DataItem){
        moveToDetailModule(data)
    }

    private fun moveToDetailModule(data: DataItem){
        val fragment = ModulDetailFragment()
        val bundle = Bundle().apply {
            putParcelable("module", data)
        }
        fragment.arguments = bundle

        val fragmentManager = requireActivity().supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_home_nav, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    private fun setupViewModel() {
        factory = ViewModelFactory.getInstance(requireContext())
    }

    override fun loadingHandler(isLoading: Boolean) {
        if (isLoading) {
            binding.loadingAnimation.visibility = View.VISIBLE
        } else {
            binding.loadingAnimation.visibility = View.GONE
        }
    }

}
