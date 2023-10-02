package com.example.moneykeeper.domain.use_cases.budget

import com.example.moneykeeper.domain.repository.BudgetRepository
import com.example.moneykeeper.domain.repository.CategoryRepository

class GetBudgets(private val repository: BudgetRepository) {

    operator fun invoke() = repository.getBudgets()
}