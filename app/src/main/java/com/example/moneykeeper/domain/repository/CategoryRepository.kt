package com.example.moneykeeper.domain.repository

import com.example.moneykeeper.domain.model.Category
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    fun getCategories(): Flow<List<Category>>

    suspend fun getCategoryById(id: Int): Category

    fun getExpenses(): Flow<List<Category>>
    fun getRevenues(): Flow<List<Category>>
}