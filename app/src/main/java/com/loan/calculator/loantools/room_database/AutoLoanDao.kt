package com.loan.calculator.loantools.room_database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.loan.calculator.loantools.model.AutoLoanModel
import com.loan.calculator.loantools.model.LoanModel
import kotlinx.coroutines.flow.Flow

@Dao
interface AutoLoanDao {
    @Insert
    suspend fun insertAutoLoan(autoLoan: AutoLoanModel): Long

    @Query("SELECT * FROM auto_loan_table ORDER BY id DESC")
    fun getAllAutoLoan(): Flow<List<AutoLoanModel>>

    @Query("DELETE FROM auto_loan_table")
    suspend fun deleteAllAutoLoan()

    @Query("DELETE FROM auto_loan_table WHERE id = :id")
    suspend fun deleteByIdAutoLoan(id: Int)

    //
    @Query("DELETE FROM auto_loan_table WHERE id = :id")
    suspend fun deleteById(id: Int)

    //
    @Query("UPDATE auto_loan_table SET nameCompareList = :name, isCompare = :isCompare WHERE id = :id")
    suspend fun updateNameAndCompareAuto(id: Int, name: String?, isCompare: Boolean)

    //
    @Query("SELECT * FROM auto_loan_table WHERE isCompare = 1 ORDER BY id DESC")
    fun getCompareListAutoLoan(): Flow<List<AutoLoanModel>>

    @Query("SELECT COUNT(*) FROM auto_loan_table WHERE isCompare = 1")
    suspend fun getCompareCount(): Int
}