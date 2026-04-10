package com.loan.calculator.loantools.room_database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.loan.calculator.loantools.model.BusinessLoanModel
import com.loan.calculator.loantools.model.LoanModel
import kotlinx.coroutines.flow.Flow

@Dao
interface BusinessLoanDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(loan: BusinessLoanModel): Long

    @Query("SELECT * FROM business_loan ORDER BY id DESC")
    fun getAllHistory(): Flow<List<BusinessLoanModel>>

    @Query("SELECT * FROM business_loan WHERE customName IS NOT NULL AND customName != ''")
    fun getCompareList(): Flow<List<BusinessLoanModel>>

    @Query("DELETE FROM business_loan WHERE id = :id")
    suspend fun deleteById(id: Int)
    //
    @Query("UPDATE business_loan SET nameCompareList = :name, isCompare = :isCompare WHERE id = :id")
    suspend fun updateNameAndCompareBusiness(id: Int, name: String?, isCompare: Boolean)
    //
    @Query("SELECT * FROM business_loan WHERE isCompare = 1 ORDER BY id DESC")
    fun getCompareListBusinessLoan(): Flow<List<BusinessLoanModel>>

    @Query("SELECT COUNT(*) FROM business_loan WHERE isCompare = 1")
    suspend fun getCompareCount(): Int
}