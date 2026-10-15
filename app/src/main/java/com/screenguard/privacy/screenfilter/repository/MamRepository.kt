package com.screenguard.privacy.screenfilter.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.screenguard.privacy.screenfilter.model.BudgetEntity
import com.screenguard.privacy.screenfilter.model.TransactionEntity
import com.screenguard.privacy.screenfilter.room_database.DatabaseBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MamRepository(context: Context) {
    private val db = DatabaseBuilder.getInstance(context)
    private val txDao = db.transactionDao()
    private val budgetDao = db.budgetDao()

    fun getAllTransactionsLive(): LiveData<List<TransactionEntity>> = txDao.getAllTransactionsLive()
    fun getAllBudgetsLive(): LiveData<List<BudgetEntity>> = budgetDao.getAllBudgetsLive()

    suspend fun insertTransaction(tx: TransactionEntity): Long = withContext(Dispatchers.IO) {
        txDao.insertTransaction(tx)
    }

    suspend fun deleteTransaction(tx: TransactionEntity) = withContext(Dispatchers.IO) {
        txDao.deleteTransaction(tx)
    }

    suspend fun deleteTransactionById(id: Long) = withContext(Dispatchers.IO) {
        txDao.deleteById(id)
    }

    suspend fun initDefaultDataIfNeeded() = withContext(Dispatchers.IO) {
        if (txDao.getCount() == 0) {
            val now = System.currentTimeMillis()
            val sampleTransactions = listOf(
                TransactionEntity(
                    title = "Phở Bò Bát Đàn",
                    type = "EXPENSE",
                    amount = 55000L,
                    category = "Ăn uống",
                    wallet = "Tiền mặt",
                    timestamp = now - 3600_000L,
                    dateGroup = "Hôm nay 24 Tháng 10",
                    timeStr = "08:15",
                    note = "Ăn sáng cùng đồng nghiệp"
                ),
                TransactionEntity(
                    title = "Grab Car đi làm",
                    type = "EXPENSE",
                    amount = 85000L,
                    category = "Di chuyển",
                    wallet = "Techcombank",
                    timestamp = now - 7200_000L,
                    dateGroup = "Hôm nay 24 Tháng 10",
                    timeStr = "07:45",
                    note = "Đi làm trời mưa"
                ),
                TransactionEntity(
                    title = "Mua sách Fahasa",
                    type = "EXPENSE",
                    amount = 225000L,
                    category = "Giáo dục",
                    wallet = "Ví MoMo",
                    timestamp = now - 1800_000L,
                    dateGroup = "Hôm nay 24 Tháng 10",
                    timeStr = "14:20",
                    note = "Sách tâm lý học hành vi"
                ),
                TransactionEntity(
                    title = "Tiền điện EVN",
                    type = "EXPENSE",
                    amount = 640000L,
                    category = "Hóa đơn",
                    wallet = "MB Bank",
                    timestamp = now - 86400_000L,
                    dateGroup = "Hôm qua 23 Tháng 10",
                    timeStr = "20:10",
                    note = "Hóa đơn điện tháng 10"
                ),
                TransactionEntity(
                    title = "Siêu thị Tops Market",
                    type = "EXPENSE",
                    amount = 600000L,
                    category = "Ăn uống",
                    wallet = "Techcombank",
                    timestamp = now - 90000_000L,
                    dateGroup = "Hôm qua 23 Tháng 10",
                    timeStr = "18:30",
                    note = "Mua thực phẩm cả tuần"
                ),
                TransactionEntity(
                    title = "Lương tháng 10/2024",
                    type = "INCOME",
                    amount = 12000000L,
                    category = "Thu nhập chính",
                    wallet = "Techcombank",
                    timestamp = now - 4 * 86400_000L,
                    dateGroup = "20 Tháng 10 Chủ nhật",
                    timeStr = "10:00",
                    note = "Lương công ty chuyển"
                ),
                TransactionEntity(
                    title = "Cà phê sáng & Bánh mì",
                    type = "EXPENSE",
                    amount = 45000L,
                    category = "Ăn uống",
                    wallet = "Tiền mặt",
                    timestamp = now - 5000_000L,
                    dateGroup = "Hôm nay 24 Tháng 10",
                    timeStr = "08:15"
                ),
                TransactionEntity(
                    title = "Siêu thị WinMart",
                    type = "EXPENSE",
                    amount = 320000L,
                    category = "Ăn uống",
                    wallet = "Ví MoMo",
                    timestamp = now - 88000_000L,
                    dateGroup = "Hôm qua 23 Tháng 10",
                    timeStr = "19:00"
                )
            )
            txDao.insertAll(sampleTransactions)
        }

        if (budgetDao.getCount() == 0) {
            val sampleBudgets = listOf(
                BudgetEntity(
                    category = "Ăn uống & Thực phẩm",
                    limitAmount = 3000000L,
                    spentAmount = 1450000L,
                    statusNote = "Đang kiểm soát tốt"
                ),
                BudgetEntity(
                    category = "Mua sắm cá nhân",
                    limitAmount = 1500000L,
                    spentAmount = 1350000L,
                    statusNote = "Sắp chạm mức (90%)",
                    warningTip = "Bạn còn 150.000 đ cho 17 ngày còn lại. Cùng thong thả hạn chế sắm đồ mới nhé!"
                ),
                BudgetEntity(
                    category = "Di chuyển & Xăng xe",
                    limitAmount = 1000000L,
                    spentAmount = 480000L,
                    statusNote = "An toàn"
                ),
                BudgetEntity(
                    category = "Giải trí & Cafe",
                    limitAmount = 800000L,
                    spentAmount = 270000L,
                    statusNote = "An toàn"
                )
            )
            budgetDao.insertAll(sampleBudgets)
        }
    }
}
