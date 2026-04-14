package com.screenguard.privacy.screenfilter.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "loan_table")
data class LoanModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nameCompareList: String? = null,
    val isCompare: Boolean = false,
    val resIdMoney: String = "USD",

    val loanAmount: Double,
    val interestRate: Double,
    val loanTerm: Int,
    val termType: String, // Month / Year
    val startDate: String,

    val emi: Double,
    val totalPayment: Double,
    val totalInterest: Double,
    val payoffDate: String,
)