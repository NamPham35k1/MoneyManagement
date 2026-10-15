package com.screenguard.privacy.screenfilter.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mam_transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val type: String, // "EXPENSE" or "INCOME"
    val amount: Long,
    val category: String,
    val wallet: String,
    val timestamp: Long,
    val dateGroup: String, // "Hôm nay 24 Tháng 10", "Hôm qua 23 Tháng 10", "20 Tháng 10 Chủ nhật"
    val timeStr: String, // "08:15", "07:45"
    val note: String = "",
    val isRecurring: Boolean = false
)
