package com.example.moneykeeper.data.repository

import com.example.moneykeeper.data.local.CategoryDao
import com.example.moneykeeper.domain.model.Category
import com.example.moneykeeper.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(private val dao: CategoryDao) : CategoryRepository {
    override fun getCategories(): Flow<List<Category>> {
        return dao.getCategories()
    }

    override suspend fun getCategoryById(id: Int): Category {
        return dao.getCategoryById(id)
    }

    override fun getExpenses(): Flow<List<Category>> {
        return dao.getExpenses()
    }

    override fun getRevenues(): Flow<List<Category>> {
        return dao.getRevenues()
    }


}