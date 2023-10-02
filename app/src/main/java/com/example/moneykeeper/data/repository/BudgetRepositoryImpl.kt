package com.example.moneykeeper.data.repository

import com.example.moneykeeper.data.local.BudgetDao
import com.example.moneykeeper.data.local.CategoryDao
import com.example.moneykeeper.domain.model.Budget
import com.example.moneykeeper.domain.model.Category
import com.example.moneykeeper.domain.repository.BudgetRepository
import com.example.moneykeeper.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BudgetRepositoryImpl @Inject constructor(private val dao: BudgetDao) : BudgetRepository {
    override fun getBudgets(): Flow<List<Budget>> {
        return dao.getBudgets()
    }

    override fun getBudgetsForYearAndMonth(monthYear: String): Flow<List<Budget>> {
        return dao.getBudgetsForYearAndMonth(monthYear)
    }

    override suspend fun insert(budget: Budget) {
        return dao.insert(budget)
    }
    override suspend fun update(budget: Budget) {
        return dao.update(budget)

    }
    override suspend fun delete(budget: Budget) {
        return dao.delete(budget)
    }


}