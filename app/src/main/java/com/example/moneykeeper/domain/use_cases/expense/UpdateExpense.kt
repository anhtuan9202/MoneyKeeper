package com.example.moneykeeper.domain.use_cases.expense

import com.example.moneykeeper.domain.model.Expense
import com.example.moneykeeper.domain.repository.ExpenseRepository


class UpdateExpense(private val repository: ExpenseRepository) {

    suspend operator fun invoke(expense: Expense) = repository.update(expense)
}