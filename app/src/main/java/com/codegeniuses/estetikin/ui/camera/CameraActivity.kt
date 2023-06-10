package com.codegeniuses.estetikin.ui.camera

import android.content.ContentUris
import android.content.ContentValues
import android.content.Intent
import android.database.ContentObserver
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.codegeniuses.estetikin.databinding.ActivityCameraBinding
import com.codegeniuses.estetikin.ui.MainActivity
import com.codegeniuses.estetikin.utils.createFile
import com.codegeniuses.estetikin.utils.uriToFile
import java.util.*

// Machine Learning Deploy
import com.codegeniuses.estetikin.ml.Model1
import com.codegeniuses.estetikin.ml.Model2
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder

class CameraActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCameraBinding

    private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    private var imageCapture: ImageCapture? = null
    var imageSize = 224

    private var gridOverlayContainer: FrameLayout? = null
    private var galleryIcon: ImageView? = null

    private var recentPhotoUri: Uri? = null
    private var mediaStoreContentObserver: ContentObserver? = null

    companion object {
        private const val PERMISSION_REQUEST_CODE = 123
        private const val GALLERY_OBSERVER_DELAY_MS = 500L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        gridOverlayContainer = binding.gridOverlayContainer
        galleryIcon = binding.galleryIcon

        // Overlaying the Android UI setting
        hideSystemUI()

        binding.galleryIcon.setOnClickListener { startGallery() }
        binding.captureImage.setOnClickListener { takePhoto() }
        binding.switchCamera.setOnClickListener {
            cameraSelector =
                if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) CameraSelector.DEFAULT_FRONT_CAMERA
                else CameraSelector.DEFAULT_BACK_CAMERA
            startCamera()
        }

        mediaStoreContentObserver = createMediaStoreContentObserver()
    }

    override fun onResume() {
        super.onResume()
        hideSystemUI()
        startCamera()
        loadRecentPhoto()
        registerContentObserver()
    }

    override fun onPause() {
        super.onPause()
        unregisterContentObserver()
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
            }

            imageCapture = ImageCapture.Builder().build()

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview,
                    imageCapture
                )
            } catch (exc: Exception) {
                Toast.makeText(
                    this@CameraActivity,
                    "Gagal memunculkan kamera.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun classifyImage(image: Bitmap) {
        try {
            val model1 = Model1.newInstance(applicationContext)
            val model2 = Model2.newInstance(applicationContext)

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
                applicationContext,
                classes1[maxPos1],
                Toast.LENGTH_SHORT
            ).show()
            Log.d("success", classes1[maxPos1])
            var s1 = ""
            for (i in classes1.indices) {
                s1 += String.format("%s: %.1f%%\n", classes1[i], confidences1[i] * 100)
            }
            Toast.makeText(applicationContext, s1, Toast.LENGTH_SHORT).show()
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
                applicationContext,
                classes2[maxPos2],
                Toast.LENGTH_SHORT
            ).show()
            Log.d("success", classes2[maxPos2])
            var s2 = ""
            for (i in classes2.indices) {
                s2 += String.format("%s: %.1f%%\n", classes2[i], confidences2[i] * 100)
            }
            Toast.makeText(applicationContext, s2, Toast.LENGTH_SHORT).show()
            Log.d("success", s2)

            // Releases model resources if no longer used.
            model1.close()
            model2.close()
        } catch (e: IOException) {
            // TODO Handle the exception
        }
    }


    private fun takePhoto() {
        val imageCapture = imageCapture ?: return

        val photoFile = createFile(application)

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Toast.makeText(
                        this@CameraActivity,
                        "Gagal mengambil gambar.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val savedUri = output.savedUri ?: Uri.fromFile(photoFile)
                    saveImageToGallery(savedUri)

                    // pasang ml di gambar hasil kamera
                    

                    val intent = Intent()
                    intent.putExtra("picture", photoFile)
                    intent.putExtra(
                        "isBackCamera",
                        cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA
                    )
                    setResult(MainActivity.CAMERA_X_RESULT, intent)
                    finish()
                }
            }
        )
    }

    private fun saveImageToGallery(uri: Uri) {
        val contentResolver = applicationContext.contentResolver
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "Image_${System.currentTimeMillis()}.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
        }

        contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
    }

    private fun loadRecentPhoto() {
        val projection = arrayOf(
            MediaStore.Images.ImageColumns._ID,
            MediaStore.Images.ImageColumns.DATE_TAKEN
        )

        val sortOrder = "${MediaStore.Images.ImageColumns.DATE_TAKEN} DESC"

        val cursor = contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            sortOrder
        )

        cursor?.use {
            if (it.moveToFirst()) {
                val columnIndexId = it.getColumnIndex(MediaStore.Images.ImageColumns._ID)
                val columnIndexDateTaken =
                    it.getColumnIndex(MediaStore.Images.ImageColumns.DATE_TAKEN)
                val imageUri = ContentUris.withAppendedId(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    it.getLong(columnIndexId)
                )
                recentPhotoUri = imageUri
                galleryIcon?.let { icon ->
                    Glide.with(this)
                        .load(imageUri)
                        .apply(RequestOptions.circleCropTransform()) // Apply circular transformation
                        .into(icon)
                }
            }
        }
    }


    private fun registerContentObserver() {
        recentPhotoUri?.let { uri ->
            mediaStoreContentObserver?.let { observer ->
                contentResolver.registerContentObserver(uri, true, observer)
            }
        }
    }

    private fun unregisterContentObserver() {
        mediaStoreContentObserver?.let {
            contentResolver.unregisterContentObserver(it)
        }
    }

    private fun createMediaStoreContentObserver(): ContentObserver {
        return object : ContentObserver(Handler(Looper.getMainLooper())) {
            override fun onChange(selfChange: Boolean, uri: Uri?) {
                super.onChange(selfChange, uri)
                loadRecentPhotoDelayed()
            }
        }
    }

    private fun loadRecentPhotoDelayed() {
        Handler(Looper.getMainLooper()).postDelayed({
            loadRecentPhoto()
        }, GALLERY_OBSERVER_DELAY_MS)
    }

    private fun startGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        launcherIntentGallery.launch(intent)
    }

    private val launcherIntentGallery =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val selectedImg = result.data?.data as Uri
                selectedImg.let { uri ->
                    val myFile = uriToFile(uri, this@CameraActivity)
                }
            }
        }

    // Overlaying the Android UI setting
    @Suppress("DEPRECATION")
    private fun hideSystemUI() {
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                )
        window.navigationBarColor = Color.TRANSPARENT
        supportActionBar?.hide()
    }
}