package com.example.moneykeeper.domain.repository

import com.example.moneykeeper.domain.model.Expense
import kotlinx.coroutines.flow.Flow
import java.util.Date

interface ExpenseRepository {
    fun getExpenses(): Flow<List<Expense>>
    fun get5Expenses(): Flow<List<Expense>>
    fun getExpensesForMonth(monthYear: String): Flow<List<Expense>>
    fun getExpensesForYear(year: String): Flow<List<Expense>>
    fun getExpensesForDay(day: String): Flow<List<Expense>>

    suspend fun insert(expense: Expense)

    suspend fun update(expense: Expense)
    suspend fun delete(expense: Expense)

    fun exportExcel()

}