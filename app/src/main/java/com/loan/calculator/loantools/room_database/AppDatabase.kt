package com.loan.calculator.loantools.room_database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.loan.calculator.loantools.model.AutoLoanModel
import com.loan.calculator.loantools.model.BusinessLoanModel
import com.loan.calculator.loantools.model.FixedDepositModel
import com.loan.calculator.loantools.model.LoanModel
import com.loan.calculator.loantools.model.MortgageModel
import com.loan.calculator.loantools.model.RecurringDepositModel
import com.loan.calculator.loantools.model.TypeWallpaperConverter

@Database(
    entities = [LoanModel::class, MortgageModel::class, BusinessLoanModel::class, AutoLoanModel::class, FixedDepositModel::class, RecurringDepositModel::class],
    version = 1,
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

}