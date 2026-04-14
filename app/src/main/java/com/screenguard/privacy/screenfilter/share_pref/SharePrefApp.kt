package com.screenguard.privacy.screenfilter.share_pref

import android.content.Context
import androidx.core.content.edit
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SharePrefApp @Inject constructor(
    @ApplicationContext context: Context
) {
    companion object {
        const val SHARED_PREF_NAME = "SharePrefConfigApp"
        const val FREE_THEME_VOICE_LOCK_IDS = "FREE_THEME_VOICE_LOCK_IDS"
        const val SELECTED_WORLD_CLOCKS = "SELECTED_WORLD_CLOCKS"

        const val FIRST_OPEN_APP = "FIRST_OPEN_APP"
        const val FIRST_OPEN_TAP_HOME = "FIRST_OPEN_TAP_HOME"
        const val RATING_APPEAR = "RatingAppear"
        const val ID_PATTERN = "ID_PATTERN"
        const val MODE = "MODE"
        const val TOGGLE_APP = "TOGGLE_APP"
        const val SWITCH_TURN_SERVICE = "SWITCH_TURN_SERVICE"

    }

    private val sharePref = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)


    var listThemeFree: Set<String>
        get() = sharePref.getStringSet(FREE_THEME_VOICE_LOCK_IDS, emptySet()) ?: emptySet()
        set(value) = sharePref.edit() { putStringSet(FREE_THEME_VOICE_LOCK_IDS, value) }

    var isFirstOpenApp: Boolean
        get() = sharePref.getBoolean(FIRST_OPEN_APP, true)
        set(value) = sharePref.edit() { putBoolean(FIRST_OPEN_APP, value) }
    var isFirstOpenTapHome: Boolean
        get() = sharePref.getBoolean(FIRST_OPEN_TAP_HOME, true)
        set(value) = sharePref.edit() { putBoolean(FIRST_OPEN_TAP_HOME, value) }

    var countRatingAppear: Int
        get() = sharePref.getInt(RATING_APPEAR, 1)
        set(value) = sharePref.edit {
            putInt(RATING_APPEAR, value)
        }
    var isRated: Boolean
        get() = sharePref.getBoolean("RATE", false)
        set(value) = sharePref.edit { putBoolean("RATE", value) }
    var idPattern: Int
        get() = sharePref.getInt(ID_PATTERN, 1)
        set(value) = sharePref.edit {
            putInt(ID_PATTERN, value)
        }
    var modeTilt: String?
        get() = sharePref.getString(MODE, "")
        set(value) = sharePref.edit {
            putString(MODE, value)
        }

    //
    var checktoggle: Boolean
        get() = sharePref.getBoolean(TOGGLE_APP, false)
        set(value) = sharePref.edit() { putBoolean(TOGGLE_APP, value) }

    var switchTurnService: Boolean
        get() = sharePref.getBoolean(SWITCH_TURN_SERVICE, true)
        set(value) = sharePref.edit() { putBoolean(SWITCH_TURN_SERVICE, value) }



    var panelOpacity: Int
        get() = sharePref.getInt("PANEL_OPACITY", 80) // default 80%
        set(value) = sharePref.edit {
            putInt("PANEL_OPACITY", value)
        }
    fun increaseCountRatingAppear() {
        val value = sharePref.getInt(RATING_APPEAR, 1)
        sharePref.edit {
            putInt(RATING_APPEAR, value + 1)
        }
    }


}