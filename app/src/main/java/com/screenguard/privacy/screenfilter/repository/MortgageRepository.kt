package com.screenguard.privacy.screenfilter.repository
import com.screenguard.privacy.screenfilter.model.LoanModel
import com.screenguard.privacy.screenfilter.model.MortgageModel
import com.screenguard.privacy.screenfilter.room_database.MortgageDao
import kotlinx.coroutines.flow.Flow

class MortgageRepository private constructor(
    private val dao: MortgageDao
) {

    suspend fun getCompareCount() = dao.getCompareCount()

    suspend fun insert(item: MortgageModel): Long {
      return  dao.insert(item)
    }
    suspend fun updateCompareMortgageLoan(id: Int, name: String, isCompare: Boolean) {
        dao.updateNameAndCompareMortgage(id, name, isCompare)
    }

     fun getAll(): Flow<List<MortgageModel>> {
        return dao.getAll()
    }
    fun getAllCompareMortgage(): Flow<List<MortgageModel>> {
        return dao.getCompareListMortgageLoan()
    }
    suspend fun delete(item: MortgageModel) {
        dao.delete(item)
    }

    suspend fun deleteAll() {
        dao.deleteAll()
    }

    companion object {
        @Volatile
        private var instance: MortgageRepository? = null

        fun getInstance(dao: MortgageDao): MortgageRepository {
            return instance ?: synchronized(this) {
                instance ?: MortgageRepository(dao).also { instance = it }
            }
        }
    }
}