package com.example.moneykeeper.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.moneykeeper.domain.model.Category
import com.example.moneykeeper.utils.Constants
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Query("Select * from ${Constants.CATEGORY}")
    fun getCategories(): Flow<List<Category>>

    @Query("Select * from ${Constants.CATEGORY} where ${Constants.CATEGORY_TYPE} like 2")
    fun getExpenses(): Flow<List<Category>>

    @Query("Select * from ${Constants.CATEGORY} where ${Constants.CATEGORY_TYPE} like 1")
    fun getRevenues(): Flow<List<Category>>

    @Query("Select * from ${Constants.CATEGORY} where ${Constants.CATEGORY_ID} = :id")
    suspend fun getCategoryById(id: Int): Category
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(category: Category)
    @Delete
    suspend fun delete(category: Category)


}