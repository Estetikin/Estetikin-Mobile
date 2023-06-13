package com.codegeniuses.estetikin.ui.home

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.codegeniuses.estetikin.R
import com.codegeniuses.estetikin.databinding.FragmentHomeBinding
import com.codegeniuses.estetikin.factory.ViewModelFactory
import com.codegeniuses.estetikin.helper.LoadingHandler
import com.codegeniuses.estetikin.ui.MainActivity
import com.codegeniuses.estetikin.ui.camera.CameraActivity
import com.codegeniuses.estetikin.ui.confirmPage.ConfirmActivity

class HomeFragment : Fragment(), LoadingHandler {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var factory: ViewModelFactory
    private val viewModel: HomeViewModel by viewModels { factory }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(LayoutInflater.from(requireActivity()))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupPermission()
        setupViewModel()
        setupNickname()
        // setup view, declare nickname = pref.getnickname
        //binding to the tv_username
        setupAction()
    }

    override fun onResume() {
        super.onResume()
        (activity as? MainActivity)?.setActionBarTitle(getString(R.string.title_home))
        val bottomNavigation: CoordinatorLayout = requireActivity().findViewById(R.id.bottom)
        bottomNavigation.visibility = View.VISIBLE
    }

    private fun setupViewModel() {
        factory = ViewModelFactory.getInstance(requireContext())
    }

    private fun setupNickname(){
        val nickname = viewModel.getNickname()
        binding.tvUserNickcname.text = nickname.toString()
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

    @Suppress("DEPRECATION")
    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    requireContext(),
                    "Cannot Find a permission.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private fun setupAction() {
        binding.tvAlbum.setOnClickListener {
            startGallery()
        }

        binding.tvCamera.setOnClickListener {
            if (allPermissionsGranted()) {
                val intent = Intent(requireContext(), CameraActivity::class.java)
                startActivity(intent)
            } else {
                setupPermission()
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


    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            val selectedImg = result.data?.data as Uri

            selectedImg.let { uri ->
                val intent = Intent(requireContext(), ConfirmActivity::class.java)
                intent.putExtra("image", uri)
                startActivity(intent)
            }
        }
    }

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}