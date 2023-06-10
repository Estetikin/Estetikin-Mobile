package com.codegeniuses.estetikin.ui.albumDetail

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.codegeniuses.estetikin.databinding.ActivityAlbumDetailBinding
import com.codegeniuses.estetikin.model.response.album.ArrAlbumItem

class AlbumDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAlbumDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlbumDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        setupAction()

        binding.ivBackButton.setOnClickListener {
            finish()
        }
    }

    private fun setupAction() {
        val album = intent.getParcelableExtra<ArrAlbumItem>("data")
        if (album != null) {
            binding.apply {
                tvRecomendationTitle.text = album.dummytext
                dateTime.text = album.formattedDate
            }
            Glide.with(binding.ivAlbumPhoto.context)
                .load(album.link)
                .into(binding.ivAlbumPhoto)
        } else {
            Toast.makeText(this, "Album data is null", Toast.LENGTH_SHORT).show()
        }
    }
}
