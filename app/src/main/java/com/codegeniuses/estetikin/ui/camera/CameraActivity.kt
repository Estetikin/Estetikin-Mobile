package com.codegeniuses.estetikin.ui.camera

import android.content.ContentUris
import android.content.ContentValues
import android.content.Intent
import android.database.ContentObserver
import android.graphics.Color
import android.net.Uri
import android.os.*
import android.provider.MediaStore
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
import com.codegeniuses.estetikin.ui.confirmPage.ConfirmActivity
import com.codegeniuses.estetikin.utils.createFile
import com.codegeniuses.estetikin.utils.uriToFile
import java.util.*

class CameraActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCameraBinding

    private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    private var imageCapture: ImageCapture? = null

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
                    this, cameraSelector, preview, imageCapture
                )
            } catch (exc: Exception) {
                Toast.makeText(
                    this@CameraActivity, "Gagal memunculkan kamera.", Toast.LENGTH_SHORT
                ).show()
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return

        val photoFile = createFile(application)

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
        imageCapture.takePicture(outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Toast.makeText(
                        this@CameraActivity, "Gagal mengambil gambar.", Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val savedUri = output.savedUri ?: Uri.fromFile(photoFile)
                    saveImageToGallery(savedUri)
                    sendImageToConfirmFragment(savedUri)
                    val intent = Intent()
                    intent.putExtra("picture", photoFile)
                    intent.putExtra(
                        "isBackCamera", cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA
                    )
                    setResult(MainActivity.CAMERA_X_RESULT, intent)
                    finish()
                }
            })
    }

    private fun sendImageToConfirmFragment(uri: Uri) {
        val intent = Intent(this, ConfirmActivity::class.java)
        intent.putExtra("image", uri)
        startActivity(intent)
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
            MediaStore.Images.ImageColumns._ID, MediaStore.Images.ImageColumns.DATE_TAKEN
        )

        val sortOrder = "${MediaStore.Images.ImageColumns.DATE_TAKEN} DESC"

        val cursor = contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null, null, sortOrder
        )

        cursor?.use {
            if (it.moveToFirst()) {
                val columnIndexId = it.getColumnIndex(MediaStore.Images.ImageColumns._ID)
                val columnIndexDateTaken =
                    it.getColumnIndex(MediaStore.Images.ImageColumns.DATE_TAKEN)
                val imageUri = ContentUris.withAppendedId(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, it.getLong(columnIndexId)
                )
                recentPhotoUri = imageUri
                galleryIcon?.let { icon ->
                    Glide.with(this).load(imageUri)
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
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            val selectedImg = result.data?.data as Uri

            selectedImg.let { uri ->
                val intent = Intent(this, ConfirmActivity::class.java)
                intent.putExtra("image", uri)
                startActivity(intent)
            }
        }
    }

    // Overlaying the Android UI setting
    @Suppress("DEPRECATION")
    private fun hideSystemUI() {
        window.decorView.systemUiVisibility =
            (View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        window.navigationBarColor = Color.TRANSPARENT
        supportActionBar?.hide()
    }
}