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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.codegeniuses.estetikin.databinding.FragmentHomeBinding
import com.codegeniuses.estetikin.helper.LoadingHandler
import com.codegeniuses.estetikin.ui.camera.CameraActivity
import java.io.IOException
import java.io.InputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder

// Machine Learning Deploy
import com.codegeniuses.estetikin.ml.Model1
import com.codegeniuses.estetikin.ml.Model2
import com.codegeniuses.estetikin.ml.Model3
import com.codegeniuses.estetikin.ml.Model4
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer

class HomeFragment : Fragment(), LoadingHandler {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by viewModels()

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

    private fun classifyImage(image: Bitmap) {
        try {
            val model1 = Model1.newInstance(requireActivity().applicationContext)
            val model2 = Model2.newInstance(requireActivity().applicationContext)
            val model3 = Model3.newInstance(requireActivity().applicationContext)
            val model4 = Model4.newInstance(requireActivity().applicationContext)

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
            // Model 1
            val outputs1: Model1.Outputs = model1.process(inputFeature0)
            val outputFeature1: TensorBuffer = outputs1.getOutputFeature0AsTensorBuffer()
            val confidences1: FloatArray = outputFeature1.getFloatArray()

            // find the index of the class with the biggest confidence.
            var maxPos1 = 0
            var maxConfidence1 = 0f
            for (i in confidences1.indices) {
                if (confidences1[i] > maxConfidence1) {
                    maxConfidence1 = confidences1[i]
                    maxPos1 = i
                }
            }

            //make the feature output data
            val classes1 = arrayOf("well-focused picture", "blurry picture")
            Toast.makeText(
                requireActivity().applicationContext,
                classes1[maxPos1],
                Toast.LENGTH_SHORT
            ).show()
            Log.d("success", classes1[maxPos1])
            var s1 = ""
            for (i in classes1.indices) {
                s1 += String.format("%s: %.1f%%\n", classes1[i], confidences1[i] * 100)
            }
            Toast.makeText(requireActivity().applicationContext, s1, Toast.LENGTH_SHORT).show()
            Log.d("success", s1)

            // Model 2
            val outputs2: Model2.Outputs = model2.process(inputFeature0)
            val outputFeature2: TensorBuffer = outputs2.getOutputFeature0AsTensorBuffer()
            val confidences2: FloatArray = outputFeature2.getFloatArray()

            // find the index of the class with the biggest confidence.
            var maxPos2 = 0
            var maxConfidence2 = 0f
            for (i in confidences2.indices) {
                if (confidences2[i] > maxConfidence2) {
                    maxConfidence2 = confidences2[i]
                    maxPos2 = i
                }
            }

            //make the feature output data
            val classes2 = arrayOf("food and drinks", "indoor", "outdoor", "modeling")
            Toast.makeText(
                requireActivity().applicationContext,
                classes2[maxPos2],
                Toast.LENGTH_SHORT
            ).show()
            Log.d("success", classes2[maxPos2])
            var s2 = ""
            for (i in classes2.indices) {
                s2 += String.format("%s: %.1f%%\n", classes2[i], confidences2[i] * 100)
            }
            Toast.makeText(requireActivity().applicationContext, s2, Toast.LENGTH_SHORT).show()
            Log.d("success", s2)

            // Model 3
            val outputs3: Model3.Outputs = model3.process(inputFeature0)
            val outputFeature3: TensorBuffer = outputs3.getOutputFeature0AsTensorBuffer()
            val confidences3: FloatArray = outputFeature3.getFloatArray()

            // find the index of the class with the biggest confidence.
            var maxPos3 = 0
            var maxConfidence3 = 0f

            //rules
            if (confidences3[1] > 70){
                // output low
                maxPos3 = 1
            } else if (confidences3[1] < 40){
                // output high
                maxPos3 = 2
            } else  {
                //output normal
                maxPos3 = 0
            }


            //make the feature output data
            val classes3 = arrayOf("normal brightness", "low brightness", "high brightness")
            Toast.makeText(
                requireActivity().applicationContext,
                classes3[maxPos3],
                Toast.LENGTH_SHORT
            ).show()
            Log.d("success", classes3[maxPos3])
            var s3 = ""
            for (i in classes3.indices) {
                s3 += String.format("%s: %.1f%%\n", classes3[i], confidences3[i] * 100)
            }
            Toast.makeText(requireActivity().applicationContext, s3, Toast.LENGTH_SHORT).show()
            Log.d("success", s3)

            // Model 4
            val outputs4: Model4.Outputs = model4.process(inputFeature0)
            val outputFeature4: TensorBuffer = outputs4.getOutputFeature0AsTensorBuffer()
            val confidences4: FloatArray = outputFeature4.getFloatArray()

            // find the index of the class with the biggest confidence.
            var maxPos4 = 0
            var maxConfidence4 = 0f
            for (i in confidences4.indices) {
                if (confidences4[i] > maxConfidence4) {
                    maxConfidence4 = confidences4[i]
                    maxPos4 = i
                }
            }

            //make the feature output data
            val classes4 = arrayOf("centered", "non-centered")
            Toast.makeText(
                requireActivity().applicationContext,
                classes4[maxPos4],
                Toast.LENGTH_SHORT
            ).show()
            Log.d("success", classes4[maxPos4])
            var s4 = ""
            for (i in classes4.indices) {
                s4 += String.format("%s: %.1f%%\n", classes4[i], confidences4[i] * 100)
            }
            Toast.makeText(requireActivity().applicationContext, s4, Toast.LENGTH_SHORT).show()
            Log.d("success", s4)

            // Releases model resources if no longer used.
            model1.close()
            model2.close()
            model3.close()
            model4.close()
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
            }
        }
    }

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}