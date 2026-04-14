package com.screenguard.privacy.screenfilter.model

data class CurrencyModel(
    val symbolRes: Int,
    val realRes: Int,
    val name: String,
    var isSelected: Boolean = false
)