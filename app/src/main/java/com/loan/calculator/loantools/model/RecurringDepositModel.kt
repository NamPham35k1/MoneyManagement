package com.loan.calculator.loantools.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.io.Serializable
@Entity(tableName = "recurring_deposit_table")
data class RecurringDepositModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nameCompareList: String? = null,
    val isCompare: Boolean = false,
    val resIdMoney: String = "USD",

    val monthlyInvestment: Double,     // P: Số tiền gửi cố định hàng tháng
    val annualInterestRate: Double,    // r: Lãi suất hàng năm (ví dụ: 6.5 cho 6.5%)
    val totalMonths: Int,              // n: Tổng số tháng gửi tiền
    val startDate: String,             // Ngày bắt đầu gửi: "dd/MM/yyyy"


    val maturityAmount: Double,        // A: Tổng giá trị nhận được khi đáo hạn
    val totalPrincipalPaid: Double,    // Tổng số tiền gốc đã đóng (P * n)
    val totalInterestEarned: Double,   // Tổng tiền lãi nhận được (A - Gốc)

    val payoffDate: String,
    val isRestarted: Boolean = false
): Serializable