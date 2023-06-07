package com.codegeniuses.estetikin.customViews

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.View

class GridOverlay(context: Context) : View(context) {

    private val linePaint: Paint = Paint().apply {
        color = Color.WHITE
        strokeWidth = 2f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val width = width
        val height = height

        val cellWidth = width / GRID_COLUMNS
        val cellHeight = height / GRID_ROWS

        // Draw vertical lines
        for (i in 1 until GRID_COLUMNS) {
            val x = i * cellWidth.toFloat()
            canvas.drawLine(x, 0f, x, height.toFloat(), linePaint)
        }

        // Draw horizontal lines
        for (i in 1 until GRID_ROWS) {
            val y = i * cellHeight.toFloat()
            canvas.drawLine(0f, y, width.toFloat(), y, linePaint)
        }
    }

    companion object {
        private const val GRID_COLUMNS = 3
        private const val GRID_ROWS = 3
    }
}
