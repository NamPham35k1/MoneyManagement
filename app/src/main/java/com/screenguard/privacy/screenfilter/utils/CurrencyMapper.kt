package com.screenguard.privacy.screenfilter.utils

import com.screenguard.privacy.screenfilter.R

object CurrencyMapper {

    fun getIconByName(name: String?): Int {
        return when (name) {
            "EUR" -> R.drawable.ic_euro
            "GBP" -> R.drawable.ic_gbd
            "AUD" -> R.drawable.ic_aud
            "USD" -> R.drawable.ic_usd
            "CNY" -> R.drawable.ic_cnd
            "INR" -> R.drawable.ic_inr
            "VND" -> R.drawable.ic_vnd
            "THB" -> R.drawable.ic_thb
            else -> R.drawable.ic_usd
        }
    }

    fun getGrayIconByName(name: String?): Int {
        return when (name) {
            "EUR" -> R.drawable.ic_gray_euro
            "GBP" -> R.drawable.ic_gray_gbd
            "AUD" -> R.drawable.ic_gray_aud
            "USD" -> R.drawable.ic_gray_usd
            "CNY" -> R.drawable.ic_gray_cnd
            "INR" -> R.drawable.ic_gray_inr
            "VND" -> R.drawable.ic_gray_vnd
            "THB" -> R.drawable.ic_gray_thb
            else -> R.drawable.ic_gray_usd
        }
    }
}