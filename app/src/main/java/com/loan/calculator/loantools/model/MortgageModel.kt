package com.loan.calculator.loantools.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "mortgage_table")
data class MortgageModel(

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    val name: String? = null,

    val nameCompareList: String? = null,
    val isCompare: Boolean = false,
    val resIdMoney: String = "USD",

    val homePrice: Double,
    val downPayment: Double,
    val loanAmount: Double,

    val interestRate: Double,
    val loanTerm: Int,
    val termType: String, // YEAR / MONTH

    val propertyTax: Double,
    val insurance: Double,
    val pmi: Double,
    val hoa: Double,

    val monthlyPayment: Double,
    val totalMonthlyPayment: Double,

    val createdAt: Long = System.currentTimeMillis()
): Serializable