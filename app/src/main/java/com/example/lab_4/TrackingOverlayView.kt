package com.example.lab_4

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.google.mlkit.vision.objects.DetectedObject

class TrackingOverlayView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {

    private var objects: List<DetectedObject> = emptyList()
    private var imageWidth = 0
    private var imageHeight = 0

    private val paint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = 6f
        isAntiAlias = true
    }

    private val textPaint = Paint().apply {
        color = Color.WHITE
        textSize = 50f
        isAntiAlias = true
    }

    private val colors = listOf(
        Color.RED, Color.GREEN, Color.BLUE, Color.CYAN, Color.MAGENTA, Color.YELLOW
    )

    fun setObjects(objects: List<DetectedObject>, imageWidth: Int, imageHeight: Int) {
        this.objects = objects
        this.imageWidth = imageWidth
        this.imageHeight = imageHeight
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val scaleX = width.toFloat() / imageWidth
        val scaleY = height.toFloat() / imageHeight
        val scale = maxOf(scaleX, scaleY)

        val dx = (width - imageWidth * scale) / 2
        val dy = (height - imageHeight * scale) / 2

        for (obj in objects) {
            paint.color = colors[obj.trackingId?.rem(colors.size) ?: 0]

            val box = obj.boundingBox
            val left = box.left * scale + dx
            val top = box.top * scale + dy
            val right = box.right * scale + dx
            val bottom = box.bottom * scale + dy

            canvas.drawRect(left, top, right, bottom, paint)

            val label = obj.labels.firstOrNull()?.text ?: "Object"
            val id = obj.trackingId ?: 0
            canvas.drawText("ID:$id $label", left, top - 10, textPaint)
        }
    }
}