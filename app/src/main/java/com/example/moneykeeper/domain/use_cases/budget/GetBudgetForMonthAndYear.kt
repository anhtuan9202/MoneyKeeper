package com.example.moneykeeper.domain.use_cases.budget

import com.example.moneykeeper.domain.repository.BudgetRepository
import com.example.moneykeeper.domain.repository.CategoryRepository

class GetBudgetForMonthAndYear(private val repository: BudgetRepository) {

    operator fun invoke(monthYear: String) = repository.getBudgetsForYearAndMonth(monthYear)
}