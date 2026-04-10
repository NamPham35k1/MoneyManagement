package com.loan.calculator.loantools.service

import android.content.Context
import android.graphics.PixelFormat
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.WindowManager
import com.loan.calculator.loantools.R
import com.loan.calculator.loantools.extention.logDebug

class PanelManager(private val context: Context) {
    private var isShowing = false
    private val windowManager =
        context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

    private var panelView: View? = null
    private var miniView: View? = null

    private val params = WindowManager.LayoutParams(
        WRAP_CONTENT,
        WRAP_CONTENT,
        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
        PixelFormat.TRANSLUCENT
    )

    fun showPanel() {
        if (isShowing) return
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

        panelView?.findViewById<View>(R.id.btnEye)?.setOnClickListener {
            showBlackScreen()
        }
        //

        panelView?.findViewById<View>(R.id.btnMiniSize)?.setOnClickListener {
            showMini()
        }
    }

    fun showMini() {
        if (miniView != null) return

        removeAll()
        logDebug("checkClick","ShowMini")

        miniView = LayoutInflater.from(context)
            .inflate(R.layout.layout_panel_mini, null)

        setupDrag(miniView!!)

        miniView?.setOnClickListener {
            logDebug("checkClick","OKDONE")
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
        } catch (e: Exception) {}

        try {
            miniView?.let { windowManager.removeView(it) }
        } catch (e: Exception) {}

        panelView = null
        miniView = null
        isShowing = false //
    }
    // all screen
    private fun setupDrag(view: View) {

        var initialX = 0
        var initialY = 0
        var initialTouchX = 0f
        var initialTouchY = 0f
        var isDragging = false

        view.setOnTouchListener { v, event ->

            when (event.action) {

                MotionEvent.ACTION_DOWN -> {
                    initialX = params.x
                    initialY = params.y
                    initialTouchX = event.rawX
                    initialTouchY = event.rawY
                    isDragging = false
                    true
                }

                MotionEvent.ACTION_MOVE -> {
                    val dx = event.rawX - initialTouchX
                    val dy = event.rawY - initialTouchY

                    if (Math.abs(dx) > 10 || Math.abs(dy) > 10) {
                        isDragging = true
                    }

                    params.x = initialX + dx.toInt()
                    params.y = initialY + dy.toInt()

                    windowManager.updateViewLayout(view, params)
                    true
                }

                MotionEvent.ACTION_UP -> {
                    if (!isDragging) {
                        v.performClick()
                    }
                    true
                }

                else -> false
            }
        }
    }
    private fun showBlackScreen(){

    }

    fun isShowing(): Boolean {
        return isShowing
    }
}