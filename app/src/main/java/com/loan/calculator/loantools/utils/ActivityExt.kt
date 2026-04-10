package com.loan.calculator.loantools.utils

import android.content.Context
import android.content.Intent
import android.os.Parcelable
import androidx.appcompat.app.AppCompatActivity
import java.io.Serializable

inline fun <reified T : AppCompatActivity> Context.startActivity(vararg pairs: Pair<String, Any?>) {
    val intent = Intent(this, T::class.java)
    pairs.forEach { (key, value) ->
        when (value) {
            is Int -> intent.putExtra(key, value)
            is Long -> intent.putExtra(key, value)
            is String -> intent.putExtra(key, value)
            is Double -> intent.putExtra(key, value)
            is Float -> intent.putExtra(key, value)
            is Boolean -> intent.putExtra(key, value)
            is Serializable -> intent.putExtra(key, value)
            is Parcelable -> intent.putExtra(key, value)
        }
    }
    startActivity(intent)
}