package com.example.moneykeeper.domain.use_cases.category

data class CategoryUseCases (
    val getCategories: GetCategories,
    val getCategoryById: GetCategoryById,
    val getExpenses: GetExpenses,
    val getRevenues: GetRevenues,
    val insertCategory: InsertCategory,
    val deleteCategory: DeleteCategory
    )