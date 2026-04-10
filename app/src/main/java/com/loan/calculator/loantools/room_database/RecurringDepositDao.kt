package com.loan.calculator.loantools.room_database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.loan.calculator.loantools.model.FixedDepositModel
import com.loan.calculator.loantools.model.LoanModel
import com.loan.calculator.loantools.model.RecurringDepositModel
import kotlinx.coroutines.flow.Flow

@Dao

interface RecurringDepositDao {
    @Insert
    suspend fun insertRecurringDeposit(recurringDeposit: RecurringDepositModel)

    @Query("SELECT * FROM recurring_deposit_table ORDER BY id DESC")
    fun getAllRecurringDeposit(): Flow<List<RecurringDepositModel>>

    @Query("DELETE FROM recurring_deposit_table")
    suspend fun deleteAllRecurringDeposit()

    @Query("DELETE FROM recurring_deposit_table WHERE id = :id")
    suspend fun deleteByIdRecurringDeposit(id: Int)
    //
    @Query("UPDATE recurring_deposit_table SET nameCompareList = :name, isCompare = :isCompare WHERE id = :id")
    suspend fun updateNameAndCompareRecurring(id: Int, name: String?, isCompare: Boolean)
    //
    @Query("SELECT * FROM recurring_deposit_table WHERE isCompare = 1 ORDER BY id DESC")
    fun getCompareListRecurringLoan(): Flow<List<RecurringDepositModel>>
}