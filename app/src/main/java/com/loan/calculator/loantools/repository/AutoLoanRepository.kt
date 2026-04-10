package com.loan.calculator.loantools.repository

import com.loan.calculator.loantools.model.AutoLoanModel
import com.loan.calculator.loantools.model.LoanModel
import com.loan.calculator.loantools.room_database.AutoLoanDao
import kotlinx.coroutines.flow.Flow

class AutoLoanRepository private constructor(
    private val autoLoanDao: AutoLoanDao
) {
    suspend fun getCompareCount() = autoLoanDao.getCompareCount()

    suspend fun insertAutoLoan(autoLoan: AutoLoanModel): Long {
       return autoLoanDao.insertAutoLoan(autoLoan)
    }
    suspend fun updateCompareAutoLoan(id: Int, name: String, isCompare: Boolean) {
        autoLoanDao.updateNameAndCompareAuto(id, name, isCompare)
    }

    fun getAllAutoLoan(): Flow<List<AutoLoanModel>> {
        return autoLoanDao.getAllAutoLoan()
    }
    fun getAllCompareAutoLoan(): Flow<List<AutoLoanModel>> {
        return autoLoanDao.getCompareListAutoLoan()
    }

    suspend fun deleteAutoLoanBYId(id:Int) {
        autoLoanDao.deleteByIdAutoLoan(id)
    }
    suspend fun deleteById(id: Int) {
        autoLoanDao.deleteById(id)
    }
    companion object {
        @Volatile
        private var INSTANCE: AutoLoanRepository? = null

        fun getInstance(autoLoanDao: AutoLoanDao): AutoLoanRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: AutoLoanRepository(autoLoanDao).also { INSTANCE = it }
            }
        }
    }
}