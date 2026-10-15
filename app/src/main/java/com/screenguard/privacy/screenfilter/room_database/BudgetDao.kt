package com.screenguard.privacy.screenfilter.room_database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.screenguard.privacy.screenfilter.model.BudgetEntity

@Dao
interface BudgetDao {
    @Query("SELECT * FROM mam_budgets")
    fun getAllBudgetsLive(): LiveData<List<BudgetEntity>>

    @Query("SELECT * FROM mam_budgets")
    suspend fun getAllBudgets(): List<BudgetEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBudget(budget: BudgetEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(budgets: List<BudgetEntity>)

    @Update
    suspend fun updateBudget(budget: BudgetEntity)

    @Query("SELECT COUNT(*) FROM mam_budgets")
    suspend fun getCount(): Int
}
