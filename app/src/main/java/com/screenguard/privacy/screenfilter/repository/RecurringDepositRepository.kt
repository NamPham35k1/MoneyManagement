package com.screenguard.privacy.screenfilter.repository

import com.screenguard.privacy.screenfilter.model.FixedDepositModel
import com.screenguard.privacy.screenfilter.model.RecurringDepositModel
import com.screenguard.privacy.screenfilter.room_database.FixedDepositDao
import com.screenguard.privacy.screenfilter.room_database.RecurringDepositDao
import kotlinx.coroutines.flow.Flow

class RecurringDepositRepository private constructor(
    private val recurringDao: RecurringDepositDao
) {

    suspend fun insertRecurringDeposit(autoLoan: RecurringDepositModel) {
        recurringDao.insertRecurringDeposit(autoLoan)
    }

    fun getAllRecurringDeposit(): Flow<List<RecurringDepositModel>> {
        return recurringDao.getAllRecurringDeposit()
    }

    suspend fun deleteRecurringDepositBYId(id:Int) {
        recurringDao.deleteByIdRecurringDeposit(id)
    }

    companion object {
        @Volatile
        private var INSTANCE: RecurringDepositRepository? = null

        fun getInstance(recurringDao: RecurringDepositDao): RecurringDepositRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: RecurringDepositRepository(recurringDao).also { INSTANCE = it }
            }
        }
    }
}