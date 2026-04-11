package com.loan.calculator.loantools.utils


import android.content.Context
import android.graphics.Color
import android.graphics.PixelFormat
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import com.loan.calculator.loantools.R

fun Context.showBlackOverlay(
    currentView: View?,
    onViewCreated: (View) -> Unit
): View? {

    val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager

    if (currentView != null) return currentView

    val blackView = LayoutInflater.from(this)
        .inflate(R.layout.layout_black_screen, null)

    blackView.setBackgroundColor(Color.BLACK)
    blackView.alpha = 1f
    val blackParams = WindowManager.LayoutParams(
        WindowManager.LayoutParams.MATCH_PARENT,
        WindowManager.LayoutParams.MATCH_PARENT,
        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE or // Quan trọng
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS or
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
        PixelFormat.TRANSLUCENT
    )

    try {
        windowManager.addView(blackView, blackParams)
        onViewCreated(blackView)
    } catch (e: Exception) {
        e.printStackTrace()
    }

    blackView.systemUiVisibility =
        View.SYSTEM_UI_FLAG_FULLSCREEN or
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
    return blackView
}

fun Context.hideOverlay(view: View?) {
    val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
    try {
        view?.let { windowManager.removeView(it) }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}