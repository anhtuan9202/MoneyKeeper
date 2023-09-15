package com.example.moneykeeper.domain.use_cases.expense

import com.example.moneykeeper.domain.repository.ExpenseRepository


class GetExpenses(private val repository: ExpenseRepository) {

    operator fun invoke() = repository.getExpenses()
}