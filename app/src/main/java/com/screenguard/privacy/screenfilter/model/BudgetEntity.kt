package com.screenguard.privacy.screenfilter.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mam_budgets")
data class BudgetEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val category: String,
    val limitAmount: Long,
    val spentAmount: Long,
    val statusNote: String,
    val warningTip: String? = null
)
