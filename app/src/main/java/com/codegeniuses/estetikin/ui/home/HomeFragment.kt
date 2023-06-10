package com.codegeniuses.estetikin.ui.home

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Bundle
import android.util.Log
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
import com.codegeniuses.estetikin.helper.LoadingHandler
import com.codegeniuses.estetikin.ml.Model
import com.codegeniuses.estetikin.ui.MainActivity
import com.codegeniuses.estetikin.ui.camera.CameraActivity
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.IOException
import java.io.InputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder

class HomeFragment : Fragment(), LoadingHandler {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by viewModels()

//    private lateinit var preferenceSelected: TextView
//    private var selectedPreference: String = ""

    var imageSize = 224

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
        setupAction()

//        preferenceSelected = binding.tvPreferencesSelected
//        binding.preferencesContainer.setOnClickListener {
//            showPreferences()
//        }
//        updatePreferencesSelected()
    }

    override fun onResume() {
        super.onResume()
        (activity as? MainActivity)?.setActionBarTitle(getString(R.string.title_home))
        val bottomNavigation: CoordinatorLayout = requireActivity().findViewById(R.id.bottom)
        bottomNavigation.visibility = View.VISIBLE
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

//    private fun showPreferences() {
//        val dialog = Dialog(requireContext())
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
//        dialog.setContentView(R.layout.item_preferences)
//
//        val ios = dialog.findViewById<TextView>(R.id.ios)
//        val android = dialog.findViewById<TextView>(R.id.android)
//        val dslr = dialog.findViewById<TextView>(R.id.dslr)
//        val mirrorless = dialog.findViewById<TextView>(R.id.mirrorless)
//
//        ios.setOnClickListener {
//            saveUserPreference("IOS")
//            dialog.dismiss()
//            makeText()
//        }
//
//        android.setOnClickListener {
//            saveUserPreference("Android")
//            dialog.dismiss()
//            makeText()
//        }
//
//        dslr.setOnClickListener {
//            saveUserPreference("DSLR")
//            dialog.dismiss()
//            makeText()
//        }
//
//        mirrorless.setOnClickListener {
//            saveUserPreference("Mirrorless")
//            dialog.dismiss()
//            makeText()
//        }
//
//        dialog.window?.apply {
//            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
//            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//            attributes?.windowAnimations = R.style.Bottom_Sheet_Animation
//            setGravity(Gravity.BOTTOM)
//        }
//
//        dialog.show()
//    }
//    private fun saveUserPreference(data: String) {
////        TODO(The Selected Preferences haven't saved on User Preferenecs)
////        TODO(Also, After I update the preferences, the article cannot fetch the article based on the preferences)
//        selectedPreference = data
//        val pref = UserPreference(requireContext())
//        pref.saveUserPreference(data)
//        updatePreferencesSelected()
//    }


//    private fun updatePreferencesSelected() {
//        if (selectedPreference.isNotEmpty()) {
//            binding.tvPreferencesSelected.text = selectedPreference
//        }
//    }
//
//    private fun makeText() {
//        if (selectedPreference.isNotEmpty()) {
//            Toast.makeText(
//                requireContext(),
//                "You chose $selectedPreference as your preference",
//                Toast.LENGTH_SHORT
//            ).show()
//        }
//    }


    override fun loadingHandler(isLoading: Boolean) {
        if (isLoading) {
            binding.loadingAnimation.visibility = View.VISIBLE
        } else {
            binding.loadingAnimation.visibility = View.GONE
        }
    }

    private fun classifyImage(image: Bitmap) {
        try {
            val model = Model.newInstance(requireActivity().applicationContext)

            // Creates inputs for reference.
            val inputFeature0: TensorBuffer =
                TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.FLOAT32)
            val byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3)
            byteBuffer.order(ByteOrder.nativeOrder())

            // get 1D array of 224 * 224 pixels in image
            val intValues = IntArray(imageSize * imageSize)
            image.getPixels(intValues, 0, image.width, 0, 0, image.width, image.height)

            // iterate over pixels and extract R, G, and B values. Add to bytebuffer.
            var pixel = 0
            for (i in 0 until imageSize) {
                for (j in 0 until imageSize) {
                    val `val` = intValues[pixel++] // RGB
                    byteBuffer.putFloat((`val` shr 16 and 0xFF) * (1f / 255f))
                    byteBuffer.putFloat((`val` shr 8 and 0xFF) * (1f / 255f))
                    byteBuffer.putFloat((`val` and 0xFF) * (1f / 255f))
                }
            }
            inputFeature0.loadBuffer(byteBuffer)

            // Runs model inference and gets result.
            val outputs: Model.Outputs = model.process(inputFeature0)
            val outputFeature0: TensorBuffer = outputs.getOutputFeature0AsTensorBuffer()
            val confidences: FloatArray = outputFeature0.getFloatArray()
            // find the index of the class with the biggest confidence.
            var maxPos = 0
            var maxConfidence = 0f
            for (i in confidences.indices) {
                if (confidences[i] > maxConfidence) {
                    maxConfidence = confidences[i]
                    maxPos = i
                }
            }
            val classes = arrayOf("well-focused picture", "blurry picture")
            Toast.makeText(
                requireActivity().applicationContext,
                classes[maxPos],
                Toast.LENGTH_SHORT
            ).show()
            Log.d("success", classes[maxPos])
//            result.setText(classes[maxPos])
            var s = ""
            for (i in classes.indices) {
                s += String.format("%s: %.1f%%\n", classes[i], confidences[i] * 100)
            }

            Toast.makeText(requireActivity().applicationContext, s, Toast.LENGTH_SHORT).show()
            Log.d("success", s)
//            confidence.setText(s)

            // Releases model resources if no longer used.
            model.close()
        } catch (e: IOException) {
            // TODO Handle the exception
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            val selectedImg = result.data?.data as Uri
            selectedImg.let { uri ->

                val inputStream: InputStream? =
                    requireActivity().contentResolver.openInputStream(uri)
                var image = BitmapFactory.decodeStream(inputStream)

                val dimension = Math.min(image.width, image.height)
                image = ThumbnailUtils.extractThumbnail(image, dimension, dimension)

                image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false)
                classifyImage(image)


//                val myFile = uriToFile(uri, requireActivity())
//                getFile = myFile
//                val uri: Uri =  // The Uri data you want to process as a Bitmap
            }
        }
    }

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}