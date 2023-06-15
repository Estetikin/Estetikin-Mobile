package com.codegeniuses.estetikin.customViews

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.drawable.Drawable
import android.net.Uri
import android.provider.MediaStore
import android.util.AttributeSet
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.ImageView


class StoryImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private val imageView: ImageView

    init {
        imageView = ImageView(context)
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        addView(imageView)
    }

    fun setImageDrawable(drawable: Drawable) {
        imageView.setImageDrawable(drawable)
    }

    fun setImageURI(uri: Uri, isBackCamera: Boolean) {
        val orientation = if (isBackCamera) 0 else 180 // Rotate by 180 degrees for front camera

        val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
        val rotatedBitmap = rotateBitmap(bitmap, orientation)

        imageView.setImageBitmap(rotatedBitmap)
    }

    private fun rotateBitmap(bitmap: Bitmap, degrees: Int): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(degrees.toFloat())
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)

        val aspectRatio =
            imageView.drawable.intrinsicWidth.toFloat() / imageView.drawable.intrinsicHeight

        val width: Int
        val height: Int

        if (aspectRatio >= 1f) {
            // Landscape image
            width = widthSize
            height = (widthSize / aspectRatio).toInt()
        } else {
            // Portrait image
            width = widthSize
            height = heightSize

            // Set scale type
            imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        }

        val finalWidthMeasureSpec = MeasureSpec.makeMeasureSpec(width, widthMode)
        val finalHeightMeasureSpec = MeasureSpec.makeMeasureSpec(height, heightMode)

        super.onMeasure(finalWidthMeasureSpec, finalHeightMeasureSpec)
        setMeasuredDimension(width, height)

        // Set layout gravity to center
        val layoutParams = layoutParams as LayoutParams
        layoutParams.gravity = Gravity.CENTER
    }

}




