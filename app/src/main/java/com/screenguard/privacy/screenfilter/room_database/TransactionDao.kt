package com.screenguard.privacy.screenfilter.room_database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.screenguard.privacy.screenfilter.model.TransactionEntity

@Dao
interface TransactionDao {
    @Query("SELECT * FROM mam_transactions ORDER BY timestamp DESC")
    fun getAllTransactionsLive(): LiveData<List<TransactionEntity>>

    @Query("SELECT * FROM mam_transactions ORDER BY timestamp DESC")
    suspend fun getAllTransactions(): List<TransactionEntity>

    @Query("SELECT * FROM mam_transactions WHERE type = :type ORDER BY timestamp DESC")
    fun getTransactionsByTypeLive(type: String): LiveData<List<TransactionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: TransactionEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(transactions: List<TransactionEntity>)

    @Delete
    suspend fun deleteTransaction(transaction: TransactionEntity)

    @Query("DELETE FROM mam_transactions WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("SELECT COUNT(*) FROM mam_transactions")
    suspend fun getCount(): Int
}
