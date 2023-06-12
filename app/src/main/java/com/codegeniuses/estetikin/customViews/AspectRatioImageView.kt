package com.codegeniuses.estetikin.customViews

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView


class AspectRatioImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val drawable = drawable
        if (drawable != null) {
            val width = MeasureSpec.getSize(widthMeasureSpec)
            val height = MeasureSpec.getSize(heightMeasureSpec)
            val aspectRatio = drawable.intrinsicWidth.toFloat() / drawable.intrinsicHeight.toFloat()

            if (width / aspectRatio > height) {
                val calculatedWidth = (height * aspectRatio).toInt()
                val newWidthMeasureSpec = MeasureSpec.makeMeasureSpec(calculatedWidth, MeasureSpec.EXACTLY)
                val newHeightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
                super.onMeasure(newWidthMeasureSpec, newHeightMeasureSpec)
            } else {
                super.onMeasure(widthMeasureSpec, heightMeasureSpec)
            }
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }
}



