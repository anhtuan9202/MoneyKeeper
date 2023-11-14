package com.example.moneykeeper.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.moneykeeper.domain.model.Budget
import com.example.moneykeeper.domain.model.Expense
import com.example.moneykeeper.presenter.utils.Constants
import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetDao {
    @Query("Select * from ${Constants.BUDGET}")
    fun getBudgets(): Flow<List<Budget>>

    @Query("SELECT * FROM ${Constants.BUDGET} WHERE substr(${Constants.BUDGET_MONTH}, 4, 10) = :monthYear")
    fun getBudgetsForYearAndMonth(monthYear: String): Flow<List<Budget>>

    @Query("SELECT * FROM ${Constants.BUDGET} ORDER BY SUBSTR(${Constants.BUDGET_MONTH}, 7) || SUBSTR(${Constants.BUDGET_MONTH}, 4, 2) || SUBSTR(${Constants.BUDGET_MONTH}, 1, 2) DESC LIMIT 5")
    fun get5Budget() : Flow<List<Budget>>

    @Insert
    suspend fun insert(budget: Budget)

    @Delete
    suspend fun delete(budget: Budget)

    @Update
    suspend fun update(budget: Budget)
}