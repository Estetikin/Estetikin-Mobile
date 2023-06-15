package com.codegeniuses.estetikin.ui.article

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.codegeniuses.estetikin.R
import com.codegeniuses.estetikin.data.local.UserPreference
import com.codegeniuses.estetikin.databinding.FragmentArticleBinding
import com.codegeniuses.estetikin.factory.ViewModelFactory
import com.codegeniuses.estetikin.helper.LoadingHandler
import com.codegeniuses.estetikin.model.response.article.ArticleItem
import com.codegeniuses.estetikin.model.result.Result
import com.codegeniuses.estetikin.ui.MainActivity
import com.codegeniuses.estetikin.ui.camera.CameraActivity

class ArticleFragment : Fragment(), LoadingHandler {

    private var _binding: FragmentArticleBinding? = null
    private val binding get() = _binding!!
    private lateinit var factory: ViewModelFactory
    private val articleViewModel: ArticleViewModel by viewModels { factory }
    private val adapter = ArticleAdapter()
    private val adapterPreference = ArticlePreferenceAdapter()
    private var isRefreshing = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArticleBinding.inflate(layoutInflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvArticle.layoutManager = layoutManager
        binding.rvArticle.adapter = adapter

        val layoutManagerPreference = LinearLayoutManager(requireContext())
        binding.rvArticleCategory.layoutManager = layoutManagerPreference
        binding.rvArticleCategory.adapter = adapterPreference

        swipeRefresh()
        setupViewModel()
        setupAction()
        setupArticle()

    }

    override fun onResume() {
        super.onResume()
        (activity as? MainActivity)?.setActionBarTitle(getString(R.string.title_article))

        val toolbar: Toolbar = requireActivity().findViewById(R.id.toolbar_container)
        toolbar.visibility = View.VISIBLE

        val bottomNavigation: CoordinatorLayout = requireActivity().findViewById(R.id.bottom)
        bottomNavigation.visibility = View.VISIBLE
    }

    private fun setupViewModel() {
        factory = ViewModelFactory.getInstance(requireContext())
    }

    private fun setupAction() {
        binding.ivArticleCover.setOnClickListener {
            if (allPermissionsGranted()) {
                val intent = Intent(requireContext(), CameraActivity::class.java)
                startActivity(intent)
            } else {
                setupPermission()
            }

        }
        adapter.setOnItemClickCallback(object : ArticleAdapter.OnItemClickCallBack {
            override fun onItemClicked(data: ArticleItem) {
                showSelectedArticle(data)
            }
        })

        adapterPreference.setOnItemPreferenceClickCallback(object :
            ArticlePreferenceAdapter.OnItemClickCallBack {
            override fun onItemClicked(data: ArticleItem) {
                showSelectedArticle(data)
            }
        })
    }

    private fun showSelectedArticle(data: ArticleItem) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(data.url)
        startActivity(intent)
    }

    private fun setupArticle() {
        isRefreshing = true
        articleViewModel.getArticles("all").observe(requireActivity()) {
            it?.let { result ->
                when (result) {
                    is Result.Loading -> {
                        loadingHandler(true)
                    }
                    is Result.Error -> {
                        loadingHandler(false)
                        Toast.makeText(
                            requireContext(),
                            "Failed to fetch article",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                    is Result.Success -> {
                        loadingHandler(false)
                        adapter.setArticleData(result.data.data)
                    }
                }
            }
        }

        val userPref = getUserPreference()

        articleViewModel.getArticles(userPref).observe(requireActivity()) {
            it?.let { result ->
                when (result) {
                    is Result.Loading -> {
                        loadingHandler(true)
                    }
                    is Result.Error -> {
                        loadingHandler(false)
                        Toast.makeText(
                            requireContext(),
                            "Failed to fetch article",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                    is Result.Success -> {
                        loadingHandler(false)
                        adapterPreference.setArticlePreferenceData(result.data.data)
                    }
                }
            }
        }

    }

    private fun getUserPreference(): String {
        val pref = UserPreference(requireContext())
        return pref.getUserPreference() ?: "all"
    }

    override fun loadingHandler(isLoading: Boolean) {
        if (isLoading) {
            binding.loadingAnimation.visibility = View.VISIBLE
        } else {
            binding.loadingAnimation.visibility = View.GONE
            if (isRefreshing) {
                binding.swipeRefresh.isRefreshing = false
                binding.swipeRefreshPreference.isRefreshing = false
                isRefreshing = false
            }
        }
    }

    private fun swipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            setupArticle()
        }
        binding.swipeRefreshPreference.setOnRefreshListener {
            setupArticle()
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    private fun setupPermission() {
        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }
    }

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}