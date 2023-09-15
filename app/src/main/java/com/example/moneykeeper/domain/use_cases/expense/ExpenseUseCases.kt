package com.example.moneykeeper.domain.use_cases.expense

data class ExpenseUseCases (
    val getExpenses: GetExpenses,
    val get5Expenses: Get5Expenses,
    val getExpensesForMonth: GetExpensesForMonth,
    val getExpensesForDay: GetExpensesForDay,
    val getExpensesForYear: GetExpensesForYear,
    val insertExpense: InsertExpense,
    val updateExpense: UpdateExpense,
    val deleteExpense: DeleteExpense
)