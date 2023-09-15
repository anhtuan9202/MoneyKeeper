package com.example.moneykeeper.data.repository

import com.example.moneykeeper.data.local.ExpenseDao
import com.example.moneykeeper.domain.model.Expense
import com.example.moneykeeper.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow
import java.util.Date
import javax.inject.Inject

class ExpenseRepositoryImpl @Inject constructor(
    private val dao: ExpenseDao
): ExpenseRepository{
    override fun getExpenses(): Flow<List<Expense>> {
        return dao.getExpenses()
    }

    override fun get5Expenses(): Flow<List<Expense>> {
        return dao.get5Expense()
    }

    override fun getExpensesForMonth(monthYear: String): Flow<List<Expense>> {
        return dao.getExpensesForYearAndMonth(monthYear)
    }

    override fun getExpensesForYear(year: String): Flow<List<Expense>> {
        return dao.getExpensesForYear(year)
    }

    override fun getExpensesForDay(day: String): Flow<List<Expense>> {
        return dao.getExpensesForDay(day)
    }


    override suspend fun insert(expense: Expense) {
        return dao.insert(expense)
    }

    override suspend fun update(expense: Expense) {
        return dao.update(expense)
    }

    override suspend fun delete(expense: Expense) {
        return dao.delete(expense)
    }


}