package com.screenguard.privacy.screenfilter.room_database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.screenguard.privacy.screenfilter.model.AutoLoanModel
import com.screenguard.privacy.screenfilter.model.FixedDepositModel
import com.screenguard.privacy.screenfilter.model.LoanModel
import kotlinx.coroutines.flow.Flow
@Dao

interface FixedDepositDao {
    @Insert
    suspend fun insertFixedDeposit(fixedDeposit: FixedDepositModel )

    @Query("SELECT * FROM fixed_deposit_table ORDER BY id DESC")
    fun getAllFixedDeposit(): Flow<List<FixedDepositModel>>

    @Query("DELETE FROM fixed_deposit_table")
    suspend fun deleteAllFixedDeposit()
    @Query("DELETE FROM fixed_deposit_table WHERE id = :id")
    suspend fun deleteByIdFixedDeposit(id: Int)
    //
    @Query("UPDATE fixed_deposit_table SET nameCompareList = :name, isCompare = :isCompare WHERE id = :id")
    suspend fun updateNameAndCompareFix(id: Int, name: String?, isCompare: Boolean)
    //
    @Query("SELECT * FROM fixed_deposit_table WHERE isCompare = 1 ORDER BY id DESC")
    fun getCompareListLoanFix(): Flow<List<FixedDepositModel>>
}