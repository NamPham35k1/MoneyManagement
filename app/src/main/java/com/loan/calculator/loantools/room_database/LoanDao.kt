package com.loan.calculator.loantools.room_database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.loan.calculator.loantools.model.LoanModel
import kotlinx.coroutines.flow.Flow

@Dao
interface LoanDao {
    @Insert
    suspend fun insertLoan(loan: LoanModel): Long

    @Query("SELECT * FROM loan_table ORDER BY id DESC")
    fun getAllLoans(): Flow<List<LoanModel>>

    @Query("DELETE FROM loan_table")
    suspend fun deleteAll()
    //
    @Query("DELETE FROM loan_table WHERE id = :id")
    suspend fun deleteById(id: Int)
    //
    @Query("UPDATE loan_table SET nameCompareList = :name, isCompare = :isCompare WHERE id = :id")
    suspend fun updateNameAndCompare(id: Int, name: String?, isCompare: Boolean)
    //
    @Query("SELECT * FROM loan_table WHERE isCompare = 1 ORDER BY id DESC")
    fun getCompareListLoan(): Flow<List<LoanModel>>

    @Query("SELECT COUNT(*) FROM loan_table WHERE isCompare = 1")
    suspend fun getCompareCount(): Int
}