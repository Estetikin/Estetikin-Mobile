package com.codegeniuses.estetikin.ui.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.codegeniuses.estetikin.databinding.ActivityProfileBinding
import com.codegeniuses.estetikin.factory.ViewModelFactory
import com.codegeniuses.estetikin.helper.LoadingHandler
import com.codegeniuses.estetikin.model.result.Result
import com.codegeniuses.estetikin.utils.reduceFileImage
import com.codegeniuses.estetikin.utils.uriToFile
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class ProfileActivity : AppCompatActivity(), LoadingHandler {
    private lateinit var binding : ActivityProfileBinding
    private lateinit var factory: ViewModelFactory
    private val viewModel: ProfileViewModel by viewModels { factory }
    private var getFile: File? = null
    private var isRefreshing = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        setContentView(binding.root)
        swipeRefresh()
    }

    override fun onResume(){
        super.onResume()
        swipeRefresh()
        setupViewModel()
        setupView()
        //setup view -> get api
        setupAction()
    }

    private fun setupViewModel() {
        factory = ViewModelFactory.getInstance(binding.root.context)
    }

    private fun setupView(){
        isRefreshing = true
        viewModel.getProfileImage().observe(this){result ->
            when (result) {
                is Result.Loading -> {
                    loadingHandler(true)
                }
                is Result.Error -> {
                    loadingHandler(false)
                    Toast.makeText(
                        this,
                        "Failed to update Profile Picture",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is Result.Success -> {
                    loadingHandler(false)
                    showProfilePicture(result.data.picture)
                }
            }
        }
    }
    private fun setupAction(){
        binding.icChangeProfilePicture.setOnClickListener {
            startGallery()
        }
    }

    private fun showProfilePicture(photoUrl: String){
        Glide.with(this)
            .load(photoUrl)
            .into(binding.ivProfilePicture)
    }

    private fun swipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            setupView()
        }
    }
    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private fun uploadProfileImage(){ val file = reduceFileImage(getFile as File)
        val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "image",
            file.name,
            requestImageFile
        )
        viewModel.uploadProfileImage(imageMultipart).observe(this){result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        loadingHandler(true)
                    }
                    is Result.Error -> {
                        loadingHandler(false)
                        Toast.makeText(this, "Failed to Upload Image", Toast.LENGTH_SHORT)
                            .show()
                    }
                    is Result.Success -> {
                        loadingHandler(false)
                        Toast.makeText(this, "Success Upload", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg = result.data?.data as Uri

            selectedImg.let { uri ->
                val myFile = uriToFile(uri, this@ProfileActivity)
                getFile = myFile
                binding.ivProfilePicture.setImageURI(uri)
                uploadProfileImage()
            }
        }
    }

    override fun loadingHandler(isLoading: Boolean) {
        if (isLoading) {
            binding.loadingAnimation.visibility = View.VISIBLE
        } else {
            binding.loadingAnimation.visibility = View.GONE
            if (isRefreshing) {
                binding.swipeRefresh.isRefreshing = false
                isRefreshing = false
            }
        }
    }
}