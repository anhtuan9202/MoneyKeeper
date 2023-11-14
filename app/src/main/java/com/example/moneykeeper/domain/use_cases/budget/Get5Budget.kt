package com.example.moneykeeper.domain.use_cases.budget

import com.example.moneykeeper.domain.repository.BudgetRepository
import com.example.moneykeeper.domain.repository.CategoryRepository

class Get5Budget(private val repository: BudgetRepository) {

    operator fun invoke() = repository.get5Budget()
}