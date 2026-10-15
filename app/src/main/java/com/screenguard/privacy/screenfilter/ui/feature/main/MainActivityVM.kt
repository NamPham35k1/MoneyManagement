package com.screenguard.privacy.screenfilter.ui.feature.main

import androidx.lifecycle.*
import com.screenguard.privacy.screenfilter.App
import com.screenguard.privacy.screenfilter.model.BudgetEntity
import com.screenguard.privacy.screenfilter.model.TransactionEntity
import com.screenguard.privacy.screenfilter.repository.MamRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityVM @Inject constructor() : ViewModel() {

    private val repository = MamRepository(App.instance)

    val transactions: LiveData<List<TransactionEntity>> = repository.getAllTransactionsLive()
    val budgets: LiveData<List<BudgetEntity>> = repository.getAllBudgetsLive()

    val isBalanceHidden = MutableLiveData(false)

    val totalIncome: LiveData<Long> = transactions.map { list ->
        list.filter { it.type == "INCOME" }.sumOf { it.amount }
    }

    val totalExpense: LiveData<Long> = transactions.map { list ->
        list.filter { it.type == "EXPENSE" }.sumOf { it.amount }
    }

    val availableBalance: LiveData<Long> = transactions.map { list ->
        val inc = list.filter { it.type == "INCOME" }.sumOf { it.amount }
        val exp = list.filter { it.type == "EXPENSE" }.sumOf { it.amount }
        // Base starting balance + net cashflow
        (inc - exp).coerceAtLeast(0L)
    }

    val safeToSpendToday: LiveData<Long> = availableBalance.map { balance ->
        // Calculated based on remaining days to salary (17 days):
        // Remaining available for safe spending divided by remaining days
        if (balance > 0) {
            val bills = 1800000L
            val savings = 1000000L
            val disposable = (balance - bills - savings).coerceAtLeast(0L)
            if (disposable > 0) disposable / 17 else 280000L
        } else {
            280000L
        }
    }

    init {
        viewModelScope.launch {
            repository.initDefaultDataIfNeeded()
        }
    }

    fun toggleBalanceVisibility() {
        isBalanceHidden.value = !(isBalanceHidden.value ?: false)
    }

    fun insertTransaction(tx: TransactionEntity) {
        viewModelScope.launch {
            repository.insertTransaction(tx)
        }
    }

    fun deleteTransaction(tx: TransactionEntity) {
        viewModelScope.launch {
            repository.deleteTransaction(tx)
        }
    }

    fun deleteTransactionById(id: Long) {
        viewModelScope.launch {
            repository.deleteTransactionById(id)
        }
    }
}
