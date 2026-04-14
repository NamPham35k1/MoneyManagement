package com.screenguard.privacy.screenfilter.model


import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "business_loan")
data class BusinessLoanModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nameCompareList: String? = null,
    val isCompare: Boolean = false,
    val resIdMoney: String = "USD",

    val loanAmount: Double,        // Số tiền vay (L)
    val annualRate: Double,        // Lãi suất năm (r)
    val numberPeriods: Int,        // Số kỳ (N)
    val frequency: Int,            // Tần suất (f) - vd: 12 cho Monthly
    val frequencyName: String,      // Tên hiển thị tần suất (Monthly, Quarterly...)

    val installment: Double,       // Trả góp mỗi kỳ
    val totalPayment: Double,      // Tổng số tiền phải trả
    val totalInterest: Double,     // Tổng lãi phải trả

    // Metadata
    val dateCreated: String,       // Ngày thực hiện tính toán
    val customName: String? = null // Tên đặt cho Result để so sánh (Compare list)
) : Serializable