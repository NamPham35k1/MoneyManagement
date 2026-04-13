package com.loan.calculator.loantools.utils

import com.loan.calculator.loantools.R
import com.loan.calculator.loantools.ui.feature.pattern.PatternModel

object PatternRepository {

    private val _patternList = listOf(
        PatternModel(1, "Pattern 1", R.drawable.pattern_1),
        PatternModel(2, "Pattern 2", R.drawable.pattern_2),
        PatternModel(3, "Pattern 3", R.drawable.pattern_3),
        PatternModel(4, "Pattern 4", R.drawable.pattern_4),
        PatternModel(5, "Pattern 5", R.drawable.pattern_5),
        PatternModel(6, "Pattern 6", R.drawable.pattern_6)
    )

    fun getAllPatterns(): List<PatternModel> {
        return _patternList
    }

    fun getDefaultPatterns(): List<PatternModel> {
        return _patternList.mapIndexed { index, model ->
            model.copy(isSelected = index == 0)
        }
    }
}