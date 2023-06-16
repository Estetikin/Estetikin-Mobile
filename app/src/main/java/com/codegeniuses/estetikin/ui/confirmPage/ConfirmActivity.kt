package com.codegeniuses.estetikin.ui.confirmPage

// Machine Learning Deploy
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.codegeniuses.estetikin.R
import com.codegeniuses.estetikin.databinding.ActivityConfirmBinding
import com.codegeniuses.estetikin.factory.ViewModelFactory
import com.codegeniuses.estetikin.helper.LoadingHandler
import com.codegeniuses.estetikin.ml.Model1
import com.codegeniuses.estetikin.ml.Model2
import com.codegeniuses.estetikin.ml.Model3
import com.codegeniuses.estetikin.ml.Model4
import com.codegeniuses.estetikin.ml.Model5
import com.codegeniuses.estetikin.model.result.Result
import com.codegeniuses.estetikin.ui.result.ResultActivity
import com.codegeniuses.estetikin.utils.reduceFileImage
import com.codegeniuses.estetikin.utils.uriToFile
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder

class ConfirmActivity : AppCompatActivity(), LoadingHandler {

    private lateinit var binding: ActivityConfirmBinding
    private val imageSize = 224
    private lateinit var factory: ViewModelFactory
    private val viewModel: ConfirmViewModel by viewModels { factory }
    private var getFile: File? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfirmBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val intent = intent
        val fileUri = intent.getParcelableExtra<Uri>("image")
        getFile = uriToFile(fileUri!!, this@ConfirmActivity)

        setupViewModel()
        setupView(fileUri)
        setupAction(fileUri)
        navigateBackButton()
    }

    private fun setupView(fileUri: Uri?) {
        if (fileUri != null) {
            val isBackCamera = intent.getBooleanExtra("isBackCamera", true)
            binding.ivYourImage.setImageURI(fileUri, isBackCamera)
        }
    }


    private fun rotateBitmap(bitmap: Bitmap, degrees: Int): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(degrees.toFloat())
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }


    private fun setupViewModel() {
        factory = ViewModelFactory.getInstance(binding.root.context)
    }

    private fun setupAction(fileUri: Uri?) {
        binding.btnSend.setOnClickListener {
            // pasang ml di gambar hasil kamera
            val inputStream: InputStream? = contentResolver.openInputStream(fileUri!!)
            var image = BitmapFactory.decodeStream(inputStream)

            val dimension = Math.min(image.width, image.height)
            image = ThumbnailUtils.extractThumbnail(image, dimension, dimension)

            image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false)
            classifyImage(fileUri, image)
        }
    }

    private fun navigateBackButton() {
        binding.backButtonContainer.setOnClickListener {
            finish()
        }
    }

    private fun classifyImage(fileUri: Uri?, image: Bitmap) {
        try {
            val model1 = Model1.newInstance(applicationContext)
            val model2 = Model2.newInstance(applicationContext)
            val model3 = Model3.newInstance(applicationContext)
            val model5 = Model5.newInstance(applicationContext)
            val model4 = Model4.newInstance(applicationContext)

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

            // Setup Model, Feature, Confidence
            // Model 1
            val outputs1: Model1.Outputs = model1.process(inputFeature0)
            val outputFeature1: TensorBuffer = outputs1.outputFeature0AsTensorBuffer
            val confidences1: FloatArray = outputFeature1.floatArray
            // Model 2
            val outputs2: Model2.Outputs = model2.process(inputFeature0)
            val outputFeature2: TensorBuffer = outputs2.outputFeature0AsTensorBuffer
            val confidences2: FloatArray = outputFeature2.floatArray
            // Model 3
            val outputs3: Model3.Outputs = model3.process(inputFeature0)
            val outputFeature3: TensorBuffer = outputs3.outputFeature0AsTensorBuffer
            val confidences3: FloatArray = outputFeature3.floatArray
            // Model 5
            val outputs5: Model5.Outputs = model5.process(inputFeature0)
            val outputFeature5: TensorBuffer = outputs5.outputFeature0AsTensorBuffer
            val confidences5: FloatArray = outputFeature5.floatArray
            // Model 4
            val outputs4: Model4.Outputs = model4.process(inputFeature0)
            val outputFeature4: TensorBuffer = outputs4.outputFeature0AsTensorBuffer
            val confidences4: FloatArray = outputFeature4.floatArray

            // Setting Rules for Predict()
            //Model 1
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
            Log.d("success", classes1[maxPos1])
            var s1 = ""
            for (i in classes1.indices) {
                s1 += String.format("%s: %.1f%%\n", classes1[i], confidences1[i] * 100)
            }

            //Model 2
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
            Log.d("success", classes2[maxPos2])
            var s2 = ""
            for (i in classes2.indices) {
                s2 += String.format("%s: %.1f%%\n", classes2[i], confidences2[i] * 100)
            }
            Log.d("success", s2)

            //Model 3
            // find the index of the class with the biggest confidence.
            var maxPos3 = 0
            var maxConfidence3 = 0f
            for (i in confidences3.indices) {
                if (confidences3[i] > maxConfidence3) {
                    maxConfidence3 = confidences3[i]
                    maxPos3 = i
                }
            }
            //Model 5
            // find the index of the class with the biggest confidence.
            var maxPos5 = 0
            var maxConfidence5 = 0f
            for (i in confidences5.indices) {
                if (confidences5[i] > maxConfidence5) {
                    maxConfidence5 = confidences5[i]
                    maxPos5 = i
                }
            }

            var maxPos35 = 3
            var low_normal = maxPos3
            var high_normal = maxPos5

            if (low_normal == 0 ){
                maxPos35 = 0
            } else {
                if (high_normal == 0){
                    maxPos35 = 2
                } else {
                    maxPos35 = 1
                }
            }

            //make the feature output data
            val classes3 = arrayOf("low brightness", "normal brightness", "high brightness")
            Log.d("success", classes3[maxPos35])
//            var s3 = ""
//            for (i in classes3.indices) {
//                s3 += String.format("%s: %.1f%%\n", classes3[i], confidences3[i] * 100)
//            }
//            Log.d("success", s3)

            //Model 4
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
            Log.d("success", classes4[maxPos4])
            var s4 = ""
            for (i in classes4.indices) {
                s4 += String.format("%s: %.1f%%\n", classes4[i], confidences4[i] * 100)
            }
            Log.d("success", s4)

            uploadImage(maxPos1, maxPos2, maxPos35, maxPos4)
            // Releases model resources if no longer used.
            model1.close()
            model2.close()
            model3.close()
            model4.close()


            moveToResultActivity(fileUri, maxPos1, maxPos2, maxPos35, maxPos4)
        } catch (e: IOException) {
            // TODO Handle the exception
        }
    }

    private fun uploadImage(class1: Int, class2: Int, class3: Int, class4: Int) {
        val file = reduceFileImage(getFile as File)
        val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "image", file.name, requestImageFile
        )

        viewModel.uploadImage(imageMultipart, class1, class2, class3, class4)
            .observe(this) { result ->
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
                            Toast.makeText(this, "Upload Success!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
    }

    private fun moveToResultActivity(
        fileUri: Uri?, class1: Int, class2: Int, class3: Int, class4: Int
    ) {
        val intent = Intent(this@ConfirmActivity, ResultActivity::class.java)
        intent.putExtra("image", fileUri)
        intent.putExtra("model1", class1)
        intent.putExtra("model2", class2)
        intent.putExtra("model3", class3)
        intent.putExtra("model4", class4)
        startActivity(intent)
    }

    override fun loadingHandler(isLoading: Boolean) {
        if (isLoading) {
            binding.loadingAnimation.visibility = View.VISIBLE
        } else {
            binding.loadingAnimation.visibility = View.GONE
        }
    }

}


