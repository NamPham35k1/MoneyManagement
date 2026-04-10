package com.loan.calculator.loantools.share_pref

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
        const val RATING_APPEAR = "RatingAppear"
        const val PERSON_LOAN = "PERSON_LOAN"
        const val BUSINESS_LOAN = "BUSINESS_LOAN"
        const val TOGGLE_APP = "TOGGLE_APP"
        const val AUTO_LOAN = "AUTO_LOAN"
        const val MORTGAGE_LOAN = "MORTGAGE_LOAN"
        const val FD_LOAN = "FD_LOAN"
        const val RD_LOAN = "RD_LOAN"

    }

    private val sharePref = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)


    var listThemeFree: Set<String>
        get() = sharePref.getStringSet(FREE_THEME_VOICE_LOCK_IDS, emptySet()) ?: emptySet()
        set(value) = sharePref.edit() { putStringSet(FREE_THEME_VOICE_LOCK_IDS, value) }

    var isFirstOpenApp: Boolean
        get() = sharePref.getBoolean(FIRST_OPEN_APP, true)
        set(value) = sharePref.edit() { putBoolean(FIRST_OPEN_APP, value) }

    var countRatingAppear: Int
        get() = sharePref.getInt(RATING_APPEAR, 1)
        set(value) = sharePref.edit {
            putInt(RATING_APPEAR, value)
        }
    var isRated: Boolean
        get() = sharePref.getBoolean("RATE", false)
        set(value) = sharePref.edit { putBoolean("RATE", value) }


    var nameMoneyPersonLoan: String
        get() = sharePref.getString(PERSON_LOAN, "USD") ?: "USD"
        set(value) = sharePref.edit {
            putString(PERSON_LOAN, value)
        }

    var nameMoneyBusinessLoan: String
        get() = sharePref.getString(BUSINESS_LOAN, "USD") ?: "USD"
        set(value) = sharePref.edit {
            putString(BUSINESS_LOAN, value)
        }

    var nameMoneyAutoLoan: String
        get() = sharePref.getString(AUTO_LOAN, "USD") ?: "USD"
        set(value) = sharePref.edit {
            putString(AUTO_LOAN, value)
        }

    var nameMoneyMortgageLoan: String
        get() = sharePref.getString(MORTGAGE_LOAN, "USD") ?: "USD"
        set(value) = sharePref.edit {
            putString(MORTGAGE_LOAN, value)
        }

    var nameMoneyFDLoan: String
        get() = sharePref.getString(FD_LOAN, "USD") ?: "USD"
        set(value) = sharePref.edit {
            putString(FD_LOAN, value)
        }

    var nameMoneyRDLoan: String
        get() = sharePref.getString(RD_LOAN, "USD") ?: "USD"
        set(value) = sharePref.edit {
            putString(RD_LOAN, value)
        }


    //
    var checktoggle: Boolean
        get() = sharePref.getBoolean(TOGGLE_APP, true)
        set(value) = sharePref.edit() { putBoolean(TOGGLE_APP, value) }



    fun increaseCountRatingAppear() {
        val value = sharePref.getInt(RATING_APPEAR, 1)
        sharePref.edit {
            putInt(RATING_APPEAR, value + 1)
        }
    }



}