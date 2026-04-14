package com.screenguard.privacy.screenfilter.repository

import com.screenguard.privacy.screenfilter.model.AutoLoanModel
import com.screenguard.privacy.screenfilter.model.FixedDepositModel
import com.screenguard.privacy.screenfilter.room_database.FixedDepositDao
import kotlinx.coroutines.flow.Flow

class FixedDepositRepository private constructor(
    private val fixDao: FixedDepositDao
) {

    suspend fun insertFixedDeposit(autoLoan: FixedDepositModel) {
        fixDao.insertFixedDeposit(autoLoan)
    }

    fun getAllFixedDeposit(): Flow<List<FixedDepositModel>> {
        return fixDao.getAllFixedDeposit()
    }

    suspend fun deleteFixedDepositBYId(id:Int) {
        fixDao.deleteByIdFixedDeposit(id)
    }

    companion object {
        @Volatile
        private var INSTANCE: FixedDepositRepository? = null

        fun getInstance(fixDao: FixedDepositDao): FixedDepositRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: FixedDepositRepository(fixDao).also { INSTANCE = it }
            }
        }
    }
}