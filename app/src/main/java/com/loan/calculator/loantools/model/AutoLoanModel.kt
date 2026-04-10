package com.loan.calculator.loantools.model


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "auto_loan_table")
data class AutoLoanModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nameCompareList: String? = null,
    val isCompare: Boolean = false,
    val resIdMoney: String = "USD",

    val loanAmount: Double,
    val interestRate: Double,    // Lãi suất năm (R)
    val loanTerm: Int,           // Thời hạn (N)
    val termType: String,        // "Month" hoặc "Year"
    val vehicleCondition: String, // "New" hoặc "Old" (Đặc thù của Auto Loan)
    val startDate: String,       // Ngày bắt đầu

    val emi: Double,             // Trả góp hàng tháng
    val totalPayment: Double,    // Tổng gốc + lãi
    val totalInterest: Double,   // Tổng lãi phải trả
    val payoffDate: String,      // Ngày tất toán dự kiến

    val createdAt: Long = System.currentTimeMillis(), // Dùng để sắp xếp lịch sử
    var compareLabel: String? = null                  // Nhãn khi cho vào Compare List
)