package com.screenguard.privacy.screenfilter.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Entity(tableName = "fixed_deposit_table")

data class FixedDepositModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nameCompareList: String? = null,
    val isCompare: Boolean = false,
    val resIdMoney: String = "USD",

    val investmentAmount: Double,      // P: Số tiền gửi ban đầu
    val annualInterestRate: Double,    // r: Lãi suất hàng năm (ví dụ: 7.5 cho 7.5%)
    val tenureYears: Double,           // t: Thời gian gửi tính bằng năm
    val startDate: String,             // Định dạng ngày: "dd/MM/yyyy"

    val accumulatedValue: Double,      // A: Tổng tiền nhận được (Gốc + Lãi)
    val totalInterestEarned: Double,   // Số tiền lãi (A - P)
    val payoffDate: String,
    val isRestarted: Boolean = false
): Serializable