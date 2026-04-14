package com.screenguard.privacy.screenfilter.extention

import android.content.Context
import android.widget.Toast

var toast: Toast? = null
fun Context.toast(text: String) {
    toast?.cancel()
    toast = Toast.makeText(this, text, Toast.LENGTH_SHORT)
    toast?.show()
}
