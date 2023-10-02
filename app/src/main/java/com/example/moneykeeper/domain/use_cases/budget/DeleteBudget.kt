package com.example.moneykeeper.domain.use_cases.budget

import com.example.moneykeeper.domain.model.Budget
import com.example.moneykeeper.domain.model.Category
import com.example.moneykeeper.domain.repository.BudgetRepository
import com.example.moneykeeper.domain.repository.CategoryRepository

class DeleteBudget(private val budgetRepository: BudgetRepository) {
    suspend operator fun invoke(budget: Budget) = budgetRepository.delete(budget)
}