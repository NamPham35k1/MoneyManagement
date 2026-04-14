package com.screenguard.privacy.screenfilter.repository

import com.screenguard.privacy.screenfilter.model.BusinessLoanModel
import com.screenguard.privacy.screenfilter.model.LoanModel
import com.screenguard.privacy.screenfilter.room_database.BusinessLoanDao
import kotlinx.coroutines.flow.Flow

class BusinessLoanRepository private constructor(
    private val businessDao: BusinessLoanDao
) {
    suspend fun getCompareCount() = businessDao.getCompareCount()

    suspend fun insertBusiness(business: BusinessLoanModel): Long {
        return businessDao.insert(business)
    }
    suspend fun updateCompareLoan(id: Int, name: String, isCompare: Boolean) {
        businessDao.updateNameAndCompareBusiness(id, name, isCompare)
    }
    fun getAllBusiness(): Flow<List<BusinessLoanModel>> {
        return businessDao.getAllHistory()
    }

    suspend fun deleteAllBusiness(id: Int) {
        businessDao.deleteById(id)
    }
    fun getAllCompareBusiness(): Flow<List<BusinessLoanModel>> {
        return businessDao.getCompareListBusinessLoan()
    }
    suspend fun deleteById(id: Int) {
        businessDao.deleteById(id)
    }

    companion object {
        @Volatile
        private var INSTANCE: BusinessLoanRepository? = null

        fun getInstance(businessDao: BusinessLoanDao): BusinessLoanRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: BusinessLoanRepository(businessDao).also { INSTANCE = it }
            }
        }
    }
}