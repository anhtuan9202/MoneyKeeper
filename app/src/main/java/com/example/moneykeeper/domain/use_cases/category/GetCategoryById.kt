package com.example.moneykeeper.domain.use_cases.category

import com.example.moneykeeper.domain.repository.CategoryRepository

class GetCategoryById(private val category: CategoryRepository) {
    suspend operator fun invoke(id: Int) = category.getCategoryById(id)
}