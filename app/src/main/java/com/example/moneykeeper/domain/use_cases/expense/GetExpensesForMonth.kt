package com.example.moneykeeper.domain.use_cases.expense

import com.example.moneykeeper.domain.repository.ExpenseRepository
import java.util.Date

class GetExpensesForMonth(private val repository: ExpenseRepository) {

    operator fun invoke(monthYear: String) = repository.getExpensesForMonth(monthYear)
}