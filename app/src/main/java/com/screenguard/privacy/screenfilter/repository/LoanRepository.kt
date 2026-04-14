package com.screenguard.privacy.screenfilter.repository

import com.screenguard.privacy.screenfilter.model.LoanModel
import com.screenguard.privacy.screenfilter.room_database.LoanDao
import kotlinx.coroutines.flow.Flow

class LoanRepository private constructor(
    private val loanDao: LoanDao
) {
    suspend fun getCompareCount() = loanDao.getCompareCount()
    suspend fun insertLoan(loan: LoanModel):Long {
        return loanDao.insertLoan(loan)
    }

    suspend fun updateCompareLoan(id: Int, name: String, isCompare: Boolean) {
        loanDao.updateNameAndCompare(id, name, isCompare)
    }

    fun getAllLoans(): Flow<List<LoanModel>> {
        return loanDao.getAllLoans()
    }

    fun getAllCompare(): Flow<List<LoanModel>> {
        return loanDao.getCompareListLoan()
    }

    suspend fun deleteAll() {
        loanDao.deleteAll()
    }

    suspend fun deleteById(id: Int) {
        loanDao.deleteById(id)
    }

    companion object {
        @Volatile
        private var INSTANCE: LoanRepository? = null

        fun getInstance(loanDao: LoanDao): LoanRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: LoanRepository(loanDao).also { INSTANCE = it }
            }
        }
    }
}