package com.screenguard.privacy.screenfilter.utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Locale
import androidx.core.widget.doAfterTextChanged
import android.text.InputFilter
import android.widget.TextView
import android.view.View
import com.screenguard.privacy.screenfilter.R

fun EditText.setupNumberFormatting() {
    this.addTextChangedListener(object : TextWatcher {
        private var current = ""

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable?) {
            if (s.toString() != current) {
                this@setupNumberFormatting.removeTextChangedListener(this)

                val cleanString = s.toString().replace(",", "")

                if (cleanString.isNotEmpty()) {
                    try {
                        val parsed = cleanString.toDouble()
                        val formatter = NumberFormat.getNumberInstance(Locale.US) as DecimalFormat
                        val formatted = formatter.format(parsed)

                        current = formatted
                        this@setupNumberFormatting.setText(formatted)
                        this@setupNumberFormatting.setSelection(formatted.length)
                    } catch (e: Exception) {}
                }

                this@setupNumberFormatting.addTextChangedListener(this)
            }
        }
    })
}
//
fun amountLimitFilter(maxDigits: Int, errorView: TextView, editText: EditText): InputFilter {
    return InputFilter { source, _, _, dest, _, _ ->
        val currentRaw = dest.toString().replace(",", "")
        val sourceRaw = source.toString().replace(",", "")

        if (currentRaw.length + sourceRaw.length > maxDigits) {
            // Hiển thị lỗi ngay khi người dùng định gõ số thứ 13
            editText.showError(errorView, true)
            ""
        } else {
            null
        }
    }
}

fun maxValueFilter(max: Double, errorView: TextView, editText: EditText): InputFilter {
    return InputFilter { source, start, end, dest, dstart, dend ->
        try {
            val input = dest.toString().substring(0, dstart) +
                    source.substring(start, end) +
                    dest.toString().substring(dend)

            if (input.isEmpty()) return@InputFilter null

            val value = input.replace(",", "").toDouble()
            if (value <= max) {
                null // Hợp lệ
            } else {
                editText.showError(errorView, true)
                ""
            }
        } catch (e: Exception) {
            ""
        }
    }
}

fun EditText.setupAmountValidation(errorView: TextView, onValidChanged: () -> Unit) {
    this.filters = arrayOf(amountLimitFilter(12, errorView, this))

    this.doAfterTextChanged { editable ->
        val input = editable?.toString() ?: ""
        val raw = input.replace(",", "")

        if (raw.isEmpty()) {
            showError(errorView, false)
            onValidChanged()
            return@doAfterTextChanged
        }

        val formatted = try {
            val parsed = raw.toLong()
            NumberFormat.getNumberInstance(Locale.US).format(parsed)
        } catch (e: Exception) { raw }

        if (formatted != input) {
            this.setText(formatted)
            this.setSelection(this.text.length)
        }

        val number = raw.toLongOrNull()
        val isInvalid = number == null || number <= 0
        showError(errorView, isInvalid)

        onValidChanged()
    }
}

fun EditText.setupInterestValidation(errorView: TextView, onValidChanged: () -> Unit) {
    this.filters = arrayOf(maxValueFilter(999.0, errorView, this))

    this.doAfterTextChanged {
        val content = it?.toString() ?: ""
        val value = content.toDoubleOrNull()
        val isInvalid = value == null || value <= 0

        showError(errorView, isInvalid && content.isNotEmpty())
        onValidChanged()
    }
}

fun EditText.setupInterestValidationMort(errorView: TextView, onValidChanged: () -> Unit) {
    this.filters = arrayOf(maxValueFilter(999.0, errorView, this))

    this.doAfterTextChanged {
        val content = it?.toString() ?: ""
        val value = content.toDoubleOrNull()

        val isInvalidValue = value != null && (value <= 0 || value > 999)

        val shouldShowError = content.isNotEmpty() && isInvalidValue

        this.showError(errorView, shouldShowError)

        onValidChanged()
    }
}

fun EditText.setupTermValidation(errorView: TextView, onValidChanged: () -> Unit) {
    this.filters = arrayOf(maxValueFilter(360.0, errorView, this))

    this.doAfterTextChanged {
        val content = it?.toString() ?: ""
        val value = content.toIntOrNull()
        val isInvalid = value == null || value < 1

        showError(errorView, isInvalid && content.isNotEmpty())
        onValidChanged()
    }
}
fun EditText.setupTermYearValidation(errorView: TextView, onValidChanged: () -> Unit) {
    this.filters = arrayOf(maxValueFilter(30.0, errorView, this))

    this.doAfterTextChanged {
        val content = it?.toString() ?: ""
        val value = content.toIntOrNull()
        val isInvalid = value == null || value < 1

        showError(errorView, isInvalid && content.isNotEmpty())
        onValidChanged()
    }
}

fun EditText.showError(errorView: TextView, isError: Boolean) {
    post {
        if (isError) {
            errorView.visibility = View.VISIBLE
            this.setBackgroundResource(R.drawable.bg_error)
        } else {
            errorView.visibility = View.INVISIBLE
            this.setBackgroundResource(R.drawable.bg_item_dark)
        }
    }
}
fun EditText.syncWith(
    targetEditText: EditText,
    getHomePrice: () -> Double,
    isToPercent: Boolean,
    syncState: SyncState
) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable?) {
            if (syncState.isUpdating) return

            val homePrice = getHomePrice()

            val inputStr = s.toString().replace(",", "")
            val input = inputStr.toDoubleOrNull()

            if (homePrice <= 0) {
                syncState.isUpdating = true
                this@syncWith.setText("")
                targetEditText.setText("")
                syncState.isUpdating = false
                return
            }

            if (inputStr.isEmpty() || input == null) {
                syncState.isUpdating = true
                targetEditText.setText("")
                syncState.isUpdating = false
                return
            }

            syncState.isUpdating = true

            try {
                if (isToPercent) {

                    if (input >= homePrice) {
                        this@syncWith.setText(String.format(Locale.US, "%,.0f", homePrice))
                        this@syncWith.setSelection(this@syncWith.text.length)

                        targetEditText.setText("100.00")
                    } else {
                        val percent = (input / homePrice) * 100
                        val formatted = String.format(Locale.US, "%.2f", percent)

                        if (targetEditText.text.toString() != formatted) {
                            targetEditText.setText(formatted)
                        }
                    }

                } else {
                    if (input >= 100) {
                        this@syncWith.setText("100")
                        this@syncWith.setSelection(this@syncWith.text.length)

                        val formatted = String.format(Locale.US, "%,.0f", homePrice)
                        targetEditText.setText(formatted)
                    } else {
                        val value = (input / 100) * homePrice
                        val formatted = String.format(Locale.US, "%,.0f", value)

                        if (targetEditText.text.toString() != formatted) {
                            targetEditText.setText(formatted)
                            targetEditText.setSelection(targetEditText.text.length)
                        }
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }

            syncState.isUpdating = false
        }
    })
}
fun String.toFriendlyName(): String {
    return this.lowercase()
        .split("_")
        .joinToString(" ") { it.replaceFirstChar { char -> char.uppercase() } }
}

fun EditText.setupDownPaymentValidation(
    errorView: TextView,
    getHomePrice: () -> Double,
    isPercent: Boolean,
    onValidChanged: () -> Unit
) {
    this.doAfterTextChanged {
        val inputStr = it?.toString()?.replace(",", "") ?: ""
        val input = inputStr.toDoubleOrNull()
        val homePrice = getHomePrice()

        if (inputStr.isEmpty()) {
            showError(errorView, false)
            onValidChanged()
            return@doAfterTextChanged
        }

        if (homePrice <= 0) {
            showError(errorView, false)
            onValidChanged()
            return@doAfterTextChanged
        }

        if (input == null) {
            showError(errorView, true)
            onValidChanged()
            return@doAfterTextChanged
        }

        val isInvalid = if (isPercent) {
            input > 100
        } else {
            input > homePrice
        }

        showError(errorView, isInvalid)
        onValidChanged()
    }
}