package com.codegeniuses.estetikin.ui.album

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.codegeniuses.estetikin.databinding.FragmentAlbumBinding
import com.codegeniuses.estetikin.factory.ViewModelFactory
import com.codegeniuses.estetikin.helper.LoadingHandler
import com.codegeniuses.estetikin.model.response.album.ArrAlbumItem
import com.codegeniuses.estetikin.model.result.Result


class AlbumFragment : Fragment(), LoadingHandler {

    private var _binding: FragmentAlbumBinding? = null
    private val binding get() = _binding!!

    private lateinit var factory: ViewModelFactory
    private val albumViewModel: AlbumViewModel by viewModels { factory }
    private val adapter = AlbumAdapter()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlbumBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.rvAlbum.layoutManager = layoutManager
        binding.rvAlbum.adapter = adapter

        setupViewModel()
        setupAlbum()
        setupAction()
    }

    private fun setupAlbum() {
        albumViewModel.getHistoryAlbum().observe(requireActivity()) {
            it?.let { result ->
                when (result) {
                    is Result.Loading -> {
                        loadingHandler(true)
                    }
                    is Result.Error -> {
                        loadingHandler(false)
                        Toast.makeText(
                            requireContext(),
                            "Failed to fetch Album",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                    is Result.Success -> {
                        loadingHandler(false)
                        adapter.setAlbumData(result.data.arrAlbum)
                    }
                }
            }
        }
    }

    private fun setupViewModel() {
        factory = ViewModelFactory.getInstance(requireContext())
    }

    private fun setupAction() {
        adapter.setOnItemClickCallback(object : AlbumAdapter.OnItemClickCallBack {
            override fun onItemClicked(data: ArrAlbumItem) {
                showSelectedAlbum(data)
            }
        })
    }

    private fun showSelectedAlbum(data: ArrAlbumItem) {
        moveToDetailAlbum(data)
    }

    private fun moveToDetailAlbum(data: ArrAlbumItem) {
        Toast.makeText(
            requireContext(),
            "Harusnya pindah ke detail ngirim extra object",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun loadingHandler(isLoading: Boolean) {
        if (isLoading) {
            binding.loadingAnimation.visibility = View.VISIBLE
        } else {
            binding.loadingAnimation.visibility = View.GONE
        }
    }
}