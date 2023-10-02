package com.example.moneykeeper.domain.use_cases.budget

data class BudgetUseCases (
    val getBudgets: GetBudgets,
    val insertBudget: InsertBudget,
    val deleteBudget: DeleteBudget,
    val updateBudget: UpdateBudget,
    val getBudgetForMonthAndYear: GetBudgetForMonthAndYear
    )