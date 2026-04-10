package com.loan.calculator.loantools.model

data class CurrencyModel(
    val symbolRes: Int,
    val realRes: Int,
    val name: String,
    var isSelected: Boolean = false
)