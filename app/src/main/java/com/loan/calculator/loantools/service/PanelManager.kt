package com.loan.calculator.loantools.service

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PixelFormat
import android.graphics.drawable.LayerDrawable
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.WindowManager
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.loan.calculator.loantools.R
import com.loan.calculator.loantools.extention.logDebug
import com.loan.calculator.loantools.utils.PatternRepository
import com.loan.calculator.loantools.utils.PrefUtils
import com.loan.calculator.loantools.utils.hideOverlay
import com.loan.calculator.loantools.utils.showBlackOverlay

class PanelManager(private val context: Context) {
    private var isShowing = false
    private val windowManager =
        context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    private var blackView: View? = null
    private var panelView: View? = null
    private var miniView: View? = null
    private var currentPrivacyType = PrivacyType.NONE

    var maxDimAlpha = 1f

    private var tiltManager: TiltPrivacyManager? = null
    private val params = WindowManager.LayoutParams(
        WRAP_CONTENT,
        WRAP_CONTENT,
        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
        PixelFormat.TRANSLUCENT
    )

    fun showPanel() {
        if (isShowing || panelView != null) return

        if (panelView?.isAttachedToWindow == true) return

        removeAll()

        panelView = LayoutInflater.from(context)
            .inflate(R.layout.layout_panel, null)

        setupDrag(panelView!!)

        try {
            windowManager.addView(panelView, params)
            isShowing = true
        } catch (e: Exception) {
            e.printStackTrace()
        }

        updateIcons()

        // tilt
        panelView?.findViewById<View>(R.id.btnFeature)?.setOnClickListener {

            if (currentPrivacyType == PrivacyType.TILT) {
                currentPrivacyType = PrivacyType.NONE
                tiltManager?.stop()
                context.hideOverlay(blackView)
                blackView = null

            } else {
                currentPrivacyType = PrivacyType.TILT

                if (tiltManager == null) {
                    tiltManager = TiltPrivacyManager(context) { shouldDim ->
                        if (currentPrivacyType != PrivacyType.TILT) return@TiltPrivacyManager
                        updateBlackViewInteraction(shouldDim)
                    }
                }

                blackView = context.showBlackOverlay(blackView) {
                    panelView?.let {
                        windowManager.removeView(it)
                        windowManager.addView(it, params)
                    }
                }

                tiltManager?.start()
            }

            updateIcons()
        }


        // eye
        panelView?.findViewById<View>(R.id.btnEye)?.setOnClickListener {

            if (currentPrivacyType == PrivacyType.EYE) {
                currentPrivacyType = PrivacyType.NONE
                hideBlackScreen()
            } else {
                currentPrivacyType = PrivacyType.EYE

                tiltManager?.stop()
                tiltManager = null

                showBlackScreen()
                blackView?.alpha = 1f
            }

            updateIcons()
        }
        // zoom
        panelView?.findViewById<View>(R.id.btnMiniSize)?.setOnClickListener {
            showMini()
        }
    }

    fun showMini() {
        if (miniView != null) return

        removeAll()
        logDebug("checkClick", "ShowMini")
        miniView = LayoutInflater.from(context)
            .inflate(R.layout.layout_panel_mini, null)

        setupDrag(miniView!!)
        updateMiniIcon()
        miniView?.setOnClickListener {
            logDebug("checkClick", "OKDONE")
            showPanel()
        }
        windowManager.addView(miniView, params)
    }

    fun hidePanel() {
        removeAll()
    }

    private fun removeAll() {
        try {
            panelView?.let { windowManager.removeView(it) }
        } catch (e: Exception) {
        }

        try {
            miniView?.let { windowManager.removeView(it) }
        } catch (e: Exception) {
        }
        panelView = null
        miniView = null
        isShowing = false //
    }


    // all screen
    @SuppressLint("ClickableViewAccessibility")
    private fun setupDrag(view: View) {
        var initialX = 0
        var initialY = 0
        var initialTouchX = 0f
        var initialTouchY = 0f
        var isDragging = false

        val touchListener = View.OnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    initialX = params.x
                    initialY = params.y
                    initialTouchX = event.rawX
                    initialTouchY = event.rawY
                    isDragging = false
                    false
                }

                MotionEvent.ACTION_MOVE -> {
                    val dx = event.rawX - initialTouchX
                    val dy = event.rawY - initialTouchY

                    if (Math.abs(dx) > 15 || Math.abs(dy) > 15) {
                        isDragging = true
                        params.x = initialX + dx.toInt()
                        params.y = initialY + dy.toInt()
                        windowManager.updateViewLayout(panelView ?: miniView ?: view, params)
                        true // Đã drag thì nuốt sự kiện để không gọi Click
                    } else {
                        false
                    }
                }

                MotionEvent.ACTION_UP -> {

                    isDragging
                }
                else -> false
            }
        }

        view.setOnTouchListener(touchListener)

        if (view is android.view.ViewGroup) {
            for (i in 0 until view.childCount) {
                val child = view.getChildAt(i)
                child.setOnTouchListener(touchListener)
            }
        }
    }
    private fun showBlackScreen() {
        if (blackView != null) return

        val inflater = LayoutInflater.from(context)
        blackView = inflater.inflate(R.layout.layout_black_screen, null)

        blackView?.setBackgroundColor(android.graphics.Color.BLACK)
        blackView?.alpha = 1f
        val blackParams = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS or
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
            PixelFormat.TRANSLUCENT
        )

        // status bar

        try {
            windowManager.addView(blackView, blackParams)

            panelView?.let {
                windowManager.removeView(it)
                windowManager.addView(it, params)
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
        blackView?.systemUiVisibility =
            View.SYSTEM_UI_FLAG_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
    }
    private fun updateMiniIcon() {
        val imgMini = miniView?.findViewById<ImageView>(R.id.miniPanel)

        when (currentPrivacyType) {
            PrivacyType.EYE -> {
                imgMini?.setImageResource(R.drawable.ic_eye_off)
            }
            PrivacyType.TILT -> {
                imgMini?.setImageResource(R.drawable.ic_phone_off)
            }
            PrivacyType.NONE -> {
                imgMini?.setImageResource(R.drawable.ic_phone_off)
            }
        }
    }
    private fun hideBlackScreen() {
        try {
            blackView?.let { windowManager.removeView(it) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        blackView = null
    }

    fun isShowing(): Boolean {
        return isShowing
    }
    private fun updateIcons() {
        val btnEye = panelView?.findViewById<ImageView>(R.id.btnEye)
        val btnFeature = panelView?.findViewById<ImageView>(R.id.btnFeature)

        when (currentPrivacyType) {
            PrivacyType.EYE -> {
                btnEye?.setImageResource(R.drawable.ic_eye_on)
                btnFeature?.setImageResource(R.drawable.ic_phone_off)
            }
            PrivacyType.TILT -> {
                btnEye?.setImageResource(R.drawable.ic_eye_off)
                btnFeature?.setImageResource(R.drawable.ic_phone_on)
            }
            PrivacyType.NONE -> {
                btnEye?.setImageResource(R.drawable.ic_eye_off)
                btnFeature?.setImageResource(R.drawable.ic_phone_off)
            }
        }
    }

    fun updateAngle(angle: Float) {
        tiltManager?.maxAngle = angle
    }


    fun updatePanelOpacity(progress: Int) {
        val alpha = (progress * 255) / 100

        val bg = panelView?.background as? LayerDrawable ?: return

        val fillDrawable = bg.getDrawable(0)
        fillDrawable.alpha = alpha
    }

    private fun updateBlackViewInteraction(shouldDim: Boolean) {
        val view = blackView ?: return
        val params = view.layoutParams as? WindowManager.LayoutParams ?: return

        if (shouldDim) {
            view.alpha = maxDimAlpha

            if (maxDimAlpha >= 1f) {
                params.flags = params.flags and WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE.inv()
            } else {
                params.flags = params.flags or WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            }
        } else {
            view.alpha = 0f
            params.flags = params.flags or WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        }

        try {
            windowManager.updateViewLayout(view, params)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

//    private fun getDrawableIdFromPattern(id: Int): Int {
//        val pattern = PatternRepository.getAllPatterns().find { it.id == id }
//        return pattern?.drawableRes ?: R.drawable.pattern_1
//    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: PanelManager? = null

        fun getInstance(context: Context): PanelManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: PanelManager(context.applicationContext).also { INSTANCE = it }
            }
        }
    }
}