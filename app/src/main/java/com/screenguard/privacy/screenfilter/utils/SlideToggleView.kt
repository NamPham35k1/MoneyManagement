package com.screenguard.privacy.screenfilter.utils

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.widget.FrameLayout
import android.widget.ImageView
import com.screenguard.privacy.screenfilter.R

class SlideToggleView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {
    private var isUserAction = true
    private var thumb: ImageView
    private var isOn = false
    private var downX = 0f
    private var dX = 0f
    private lateinit var container: FrameLayout
    var onToggleChanged: ((Boolean) -> Unit)? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.view_slide_toggle, this, true)

        thumb = findViewById(R.id.thumb)
        container = findViewById(R.id.root)
        setupTouch()
        setupClick()
    }
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        post { animateThumb() }
    }
    private fun setupTouch() {
        thumb.setOnTouchListener { view, event ->
            when (event.action) {

                MotionEvent.ACTION_DOWN -> {
                    downX = event.rawX
                    dX = view.x - downX
                    true
                }

                MotionEvent.ACTION_MOVE -> {
                    val newX = event.rawX + dX
                    val maxX = width - view.width
                    view.x = newX.coerceIn(0f, maxX.toFloat())
                    true
                }

                MotionEvent.ACTION_UP -> {
                    val center = width / 2
                    isOn = view.x > center - view.width / 2
                    animateThumb()
                    notifyState()
                    true
                }

                else -> false
            }
        }
    }

    private fun setupClick() {
        setOnClickListener {
            isOn = !isOn
            animateThumb()
            notifyState()
        }
    }

    private fun animateThumb() {
        val targetX = if (isOn)
            (width - thumb.width).toFloat()
        else
            0f

        thumb.animate()
            .x(targetX)
            .setDuration(200)
            .start()

        thumb.setImageResource(
            if (isOn) R.drawable.thumb_on else R.drawable.thumb_off
        )
        container.setBackgroundResource(
            if (isOn) R.drawable.track_on else R.drawable.track_off
        )
    }

    private fun notifyState() {
        if (isUserAction) {
            onToggleChanged?.invoke(isOn)
        }
    }

    // Public API
    fun setChecked(value: Boolean) {
        if (isOn == value) return

        isUserAction = false
        isOn = value

        post {
            animateThumb()
            isUserAction = true
        }
    }

    fun isChecked(): Boolean = isOn
}