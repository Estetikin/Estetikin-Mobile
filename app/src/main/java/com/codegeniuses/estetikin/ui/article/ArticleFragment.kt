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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.codegeniuses.estetikin.data.local.UserPreference
import com.codegeniuses.estetikin.databinding.FragmentArticleBinding
import com.codegeniuses.estetikin.factory.ViewModelFactory
import com.codegeniuses.estetikin.helper.LoadingHandler
import com.codegeniuses.estetikin.model.response.article.ArticleItem
import com.codegeniuses.estetikin.model.result.Result
import com.codegeniuses.estetikin.ui.camera.CameraActivity

class ArticleFragment : Fragment(), LoadingHandler {

    private var _binding: FragmentArticleBinding? = null
    private val binding get() = _binding!!
    private lateinit var factory: ViewModelFactory
    private val articleViewModel: ArticleViewModel by viewModels { factory }
    private val adapter = ArticleAdapter()
    private val adapterPreference = ArticlePreferenceAdapter()

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

        setupViewModel()
        setupAction()
        setupArticle()

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
        return pref.getUserPreference() ?:"all"
    }

    override fun loadingHandler(isLoading: Boolean) {
        if (isLoading) {
            binding.loadingAnimation.visibility = View.VISIBLE
        } else {
            binding.loadingAnimation.visibility = View.GONE
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