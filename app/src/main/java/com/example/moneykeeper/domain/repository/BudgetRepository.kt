package com.example.moneykeeper.domain.repository

import com.example.moneykeeper.domain.model.Budget
import com.example.moneykeeper.domain.model.Expense
import kotlinx.coroutines.flow.Flow
import java.util.Date

interface BudgetRepository {
    fun getBudgets(): Flow<List<Budget>>
    fun getBudgetsForYearAndMonth(monthYear: String): Flow<List<Budget>>
    suspend fun insert(budget: Budget)
    suspend fun update(budget: Budget)
    suspend fun delete(budget: Budget)

}