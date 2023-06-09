package com.codegeniuses.estetikin.ui.albumDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.codegeniuses.estetikin.databinding.FragmentAlbumDetailBinding
import com.codegeniuses.estetikin.model.response.album.ArrAlbumItem

class AlbumDetailFragment : Fragment() {

    private var _binding: FragmentAlbumDetailBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlbumDetailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideSupportActionBar()
        setupAction()
    }


    private fun hideSupportActionBar() {
        (requireActivity() as AppCompatActivity).supportActionBar?.show()
    }

    private fun setupAction() {
        val album = arguments?.getParcelable<ArrAlbumItem>("data") ?: null
        if (album != null) {
            binding.apply {
                tvRecomendationTitle.text = album.dummytext
                dateTime.text = album.formattedDate
            }
            Glide.with(binding.ivAlbumPhoto.context)
                .load(album.link)
                .into(binding.ivAlbumPhoto)
        }

//        binding.ivBackButton.setOnClickListener{
//           findNavController().navigate(R.id.action_albumDetailFragment_to_albumFragment)
//        }
    }
}