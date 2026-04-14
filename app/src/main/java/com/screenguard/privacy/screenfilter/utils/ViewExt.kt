package com.screenguard.privacy.screenfilter.utils


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
import android.widget.EditText
import android.widget.PopupWindow
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
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