package com.example.moneykeeper.domain.use_cases.expense

import com.example.moneykeeper.domain.repository.ExpenseRepository
import java.util.Date

class GetExpensesForYear(private val repository: ExpenseRepository) {

    operator fun invoke(year: String) = repository.getExpensesForYear(year)
}