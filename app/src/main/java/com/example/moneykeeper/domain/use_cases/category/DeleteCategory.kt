package com.example.moneykeeper.domain.use_cases.category

import com.example.moneykeeper.domain.model.Category
import com.example.moneykeeper.domain.repository.CategoryRepository

class DeleteCategory(private val categoryRepository: CategoryRepository) {
    suspend operator fun invoke(category: Category) = categoryRepository.delete(category)
}