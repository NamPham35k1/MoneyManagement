package com.screenguard.privacy.screenfilter.utils

import android.content.Context

object PrefUtils {
    private const val PREF_NAME = "app_prefs"
    private const val KEY_MODE = "mode_tilt"

    fun getTiltMode(context: Context): String {
        val pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return pref.getString(KEY_MODE, "BOTH") ?: "BOTH"
    }
}