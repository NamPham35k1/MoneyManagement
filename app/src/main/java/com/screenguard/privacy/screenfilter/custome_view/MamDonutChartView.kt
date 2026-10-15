package com.screenguard.privacy.screenfilter.custome_view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View

class MamDonutChartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    data class Segment(
        val percentage: Float,
        val color: Int
    )

    private var segments: List<Segment> = listOf(
        Segment(41f, Color.parseColor("#2D6A4F")), // Ăn uống (Green)
        Segment(38f, Color.parseColor("#D4A373")), // Mua sắm (Amber)
        Segment(14f, Color.parseColor("#556B5A")), // Di chuyển (Dark slate)
        Segment(7f, Color.parseColor("#80C29F"))   // Giải trí (Mint)
    )

    private val strokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
    }

    private val labelPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textAlign = Paint.Align.CENTER
        color = Color.parseColor("#7A857A")
    }

    private val titlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textAlign = Paint.Align.CENTER
        color = Color.parseColor("#1B4332")
        typeface = Typeface.DEFAULT_BOLD
    }

    private val percentPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textAlign = Paint.Align.CENTER
        color = Color.parseColor("#1A1D1A")
        typeface = Typeface.DEFAULT_BOLD
    }

    private val oval = RectF()

    fun setSegments(newSegments: List<Segment>) {
        this.segments = newSegments
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val size = Math.min(width, height).toFloat()
        if (size <= 0) return

        val strokeWidth = size * 0.16f
        strokePaint.strokeWidth = strokeWidth

        val padding = strokeWidth / 2f + 4f
        val left = (width - size) / 2f + padding
        val top = (height - size) / 2f + padding
        val right = left + size - padding * 2
        val bottom = top + size - padding * 2
        oval.set(left, top, right, bottom)

        // Draw segments with gap
        var startAngle = -90f
        val gapAngle = 4f
        val totalSegments = segments.size

        segments.forEach { seg ->
            val sweepAngle = (seg.percentage / 100f) * 360f - gapAngle
            strokePaint.color = seg.color
            if (sweepAngle > 0) {
                canvas.drawArc(oval, startAngle + gapAngle / 2f, sweepAngle, false, strokePaint)
            }
            startAngle += (seg.percentage / 100f) * 360f
        }

        // Draw center labels
        val centerX = width / 2f
        val centerY = height / 2f

        labelPaint.textSize = size * 0.08f
        titlePaint.textSize = size * 0.10f
        percentPaint.textSize = size * 0.11f

        canvas.drawText("Nhiều nhất", centerX, centerY - size * 0.09f, labelPaint)
        canvas.drawText("Ăn uống", centerX, centerY + size * 0.03f, titlePaint)
        canvas.drawText("41%", centerX, centerY + size * 0.16f, percentPaint)
    }
}
