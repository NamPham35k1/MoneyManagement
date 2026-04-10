package com.loan.calculator.loantools.room_database

import androidx.room.*
import com.loan.calculator.loantools.model.LoanModel
import com.loan.calculator.loantools.model.MortgageModel
import kotlinx.coroutines.flow.Flow

@Dao
interface MortgageDao {

    @Insert
    suspend fun insert(item: MortgageModel): Long

    @Query("SELECT * FROM mortgage_table ORDER BY createdAt DESC")
     fun getAll(): Flow<List<MortgageModel>>

    @Delete
    suspend fun delete(item: MortgageModel)

    @Query("DELETE FROM mortgage_table")
    suspend fun deleteAll()
    //
    @Query("UPDATE mortgage_table SET nameCompareList = :name, isCompare = :isCompare WHERE id = :id")
    suspend fun updateNameAndCompareMortgage(id: Int, name: String?, isCompare: Boolean)
    //
    @Query("SELECT * FROM mortgage_table WHERE isCompare = 1 ORDER BY id DESC")
    fun getCompareListMortgageLoan(): Flow<List<MortgageModel>>


    @Query("SELECT COUNT(*) FROM mortgage_table WHERE isCompare = 1")
    suspend fun getCompareCount(): Int
}