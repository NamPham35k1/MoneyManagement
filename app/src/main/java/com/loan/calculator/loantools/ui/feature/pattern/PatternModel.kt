package com.loan.calculator.loantools.ui.feature.pattern

data class PatternModel(
    val id: Int,
    val title: String,
    val imageResId: Int,
    var isSelected: Boolean = false
)