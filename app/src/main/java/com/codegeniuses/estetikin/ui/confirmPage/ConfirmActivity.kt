package com.codegeniuses.estetikin.ui.confirmPage

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.codegeniuses.estetikin.databinding.ActivityConfirmBinding
import com.codegeniuses.estetikin.ui.result.ResultActivity

// Machine Learning Deploy
import com.codegeniuses.estetikin.ml.Model1
import com.codegeniuses.estetikin.ml.Model2
import com.codegeniuses.estetikin.ml.Model3
import com.codegeniuses.estetikin.ml.Model4
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.IOException
import java.io.InputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.ThumbnailUtils
import android.util.Log

class ConfirmActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConfirmBinding
    private val imageSize = 224

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfirmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent
        val fileUri = intent.getParcelableExtra<Uri>("image")

        setupView(fileUri)
        setupAction(fileUri)
    }

    private fun setupView(fileUri: Uri?) {
        if (fileUri != null) {
            binding.ivYourImage.setImageURI(fileUri)
        }
    }

    private fun setupAction(fileUri: Uri?) {
        binding.btnSend.setOnClickListener {
            // pasang ml di gambar hasil kamera
            val inputStream: InputStream? = contentResolver.openInputStream(fileUri!!)
            var image = BitmapFactory.decodeStream(inputStream)

            val dimension = Math.min(image.width, image.height)
            image = ThumbnailUtils.extractThumbnail(image, dimension, dimension)

            image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false)
            classifyImage(image)
            moveToResultActivity(fileUri)
        }
    }

    private fun classifyImage(image: Bitmap) {
        try {
            val model1 = Model1.newInstance(applicationContext)
            val model2 = Model2.newInstance(applicationContext)
            val model3 = Model3.newInstance(applicationContext)
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

            // Model 3
            val outputs3: Model3.Outputs = model3.process(inputFeature0)
            val outputFeature3: TensorBuffer = outputs3.getOutputFeature0AsTensorBuffer()
            val confidences3: FloatArray = outputFeature3.getFloatArray()

            // find the index of the class with the biggest confidence.
            var maxPos3 = 0
            var maxConfidence3 = 0f
            for (i in confidences3.indices) {
                if (confidences3[i] > maxConfidence3) {
                    maxConfidence3 = confidences3[i]
                    maxPos3 = i
                }
            }

            //make the feature output data
            val classes3 = arrayOf("normal brightness", "low brightness", "high brightness")
            Toast.makeText(
                applicationContext,
                classes3[maxPos3],
                Toast.LENGTH_SHORT
            ).show()
            Log.d("success", classes3[maxPos3])
            var s3 = ""
            for (i in classes3.indices) {
                s3 += String.format("%s: %.1f%%\n", classes3[i], confidences3[i] * 100)
            }
            Toast.makeText(applicationContext, s3, Toast.LENGTH_SHORT).show()
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
                applicationContext,
                classes4[maxPos4],
                Toast.LENGTH_SHORT
            ).show()
            Log.d("success", classes4[maxPos4])
            var s4 = ""
            for (i in classes4.indices) {
                s4 += String.format("%s: %.1f%%\n", classes4[i], confidences4[i] * 100)
            }
            Toast.makeText(applicationContext, s4, Toast.LENGTH_SHORT).show()
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

    private fun moveToResultActivity(fileUri: Uri?){
        val intent = Intent(this@ConfirmActivity, ResultActivity::class.java)
        intent.putExtra("image", fileUri)
        startActivity(intent)
    }

}


