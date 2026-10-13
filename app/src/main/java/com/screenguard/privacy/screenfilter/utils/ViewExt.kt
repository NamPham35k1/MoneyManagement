package com.screenguard.privacy.screenfilter.utils


import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Context
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.EditText
import android.widget.PopupWindow
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import com.vifarmer.ads.lib.Utils.RemoteConfigHelper

import com.screenguard.privacy.screenfilter.R
import java.text.NumberFormat
import java.util.Calendar


fun Long.toDateTimeString(format: String = "dd/MM/yyyy"): String {
    val date = Date(this)
    val sdf = SimpleDateFormat(format, Locale.getDefault())
    return sdf.format(date)
}

fun Context.getAppVersionNameId(): String {
    return packageManager.getPackageInfo(packageName, 0).versionName ?: "unknown"
}

fun EditText.isValidAmount(maxLength: Int = 12): Boolean {
    val value = text.toString().replace(",", "")
    return value.isNotEmpty() &&
            value.length <= maxLength &&
            value.toLongOrNull() != null &&
            value.toLong() > 0
}
fun EditText.isValidInterest(): Boolean {
    val value = text.toString()
    val number = value.toDoubleOrNull()

    return number != null && number > 0 && number < 999
}
fun EditText.isValidLoanTerm(max: Int = 160): Boolean {
    val value = text.toString()
    val number = value.toIntOrNull()

    return number != null && number > 0 && number < max
}

fun EditText.setToday(format: String = "dd/MM/yyyy") {
    val calendar = Calendar.getInstance()
    val sdf = SimpleDateFormat(format, Locale.getDefault())
    this.setText(sdf.format(calendar.time))
}
fun String.calculatePayoffDate(
    term: Int,
    isYear: Boolean,
    pattern: String = "d/M/yyyy"
): String {
    return runCatching {
        val sdf = java.text.SimpleDateFormat(pattern, java.util.Locale.getDefault())
        val date = sdf.parse(this) ?: return ""

        val calendar = java.util.Calendar.getInstance().apply {
            time = date
        }

        val totalMonths = if (isYear) term * 12 else term

        calendar.add(java.util.Calendar.MONTH, totalMonths)
        calendar.add(java.util.Calendar.DAY_OF_MONTH, 1)

        sdf.format(calendar.time)
    }.getOrElse { "" }
}

fun View.startCtaZoomAnimation(scaleFactor: Float = 1.05f, durationMs: Long = 800L) {
    val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1f, scaleFactor)
    val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f, scaleFactor)
    ObjectAnimator.ofPropertyValuesHolder(this, scaleX, scaleY).apply {
        duration = durationMs
        repeatCount = ObjectAnimator.INFINITE
        repeatMode = ObjectAnimator.REVERSE
        interpolator = AccelerateDecelerateInterpolator()
        start()
    }
}
fun View.setupCtaZoomAnimation() {
    val isZoomEnabled = RemoteConfigHelper.getInstance().get_config(context, Constants.RemoteKeys.ani_cta_native)
    if (!isZoomEnabled) return

    val ctaViews = mutableListOf<View>()
    fun findCtaViews(v: View) {
        if (v.id == R.id.ad_call_to_action) {
            ctaViews.add(v)
        }
        if (v is ViewGroup) {
            for (i in 0 until v.childCount) {
                findCtaViews(v.getChildAt(i))
            }
        }
    }
    findCtaViews(this)
    for (cta in ctaViews) {
        cta.startCtaZoomAnimation()
    }

    if (this is ViewGroup) {
        setOnHierarchyChangeListener(object : ViewGroup.OnHierarchyChangeListener {
            override fun onChildViewAdded(parent: View?, child: View?) {
                child?.let {
                    if (!RemoteConfigHelper.getInstance().get_config(context, Constants.RemoteKeys.ani_cta_native)) {
                        return
                    }
                    val childCtas = mutableListOf<View>()
                    fun findInChild(v: View) {
                        if (v.id == R.id.ad_call_to_action) {
                            childCtas.add(v)
                        }
                        if (v is ViewGroup) {
                            for (i in 0 until v.childCount) {
                                findInChild(v.getChildAt(i))
                            }
                        }
                    }
                    findInChild(it)
                    for (c in childCtas) {
                        c.startCtaZoomAnimation()
                    }
                }
            }

            override fun onChildViewRemoved(parent: View?, child: View?) {}
        })
    }
}