package com.screenguard.privacy.screenfilter.room_database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.screenguard.privacy.screenfilter.model.AutoLoanModel
import com.screenguard.privacy.screenfilter.model.BusinessLoanModel
import com.screenguard.privacy.screenfilter.model.FixedDepositModel
import com.screenguard.privacy.screenfilter.model.LoanModel
import com.screenguard.privacy.screenfilter.model.MortgageModel
import com.screenguard.privacy.screenfilter.model.RecurringDepositModel
import com.screenguard.privacy.screenfilter.model.TypeWallpaperConverter

import com.screenguard.privacy.screenfilter.model.TransactionEntity
import com.screenguard.privacy.screenfilter.model.BudgetEntity

@Database(
    entities = [
        LoanModel::class, MortgageModel::class, BusinessLoanModel::class,
        AutoLoanModel::class, FixedDepositModel::class, RecurringDepositModel::class,
        TransactionEntity::class, BudgetEntity::class
    ],
    version = 2,
    exportSchema = false
)
@TypeConverters(TypeWallpaperConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dao(): LoanDao
    abstract fun daoMortgage(): MortgageDao
    abstract fun daoBusiness(): BusinessLoanDao
    abstract fun daoAutoLoan(): AutoLoanDao
    abstract fun daoFixDeposit(): FixedDepositDao
    abstract fun daoRecurring(): RecurringDepositDao
    abstract fun transactionDao(): TransactionDao
    abstract fun budgetDao(): BudgetDao
}