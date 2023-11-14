package com.example.moneykeeper.data.local

import androidx.room.*
import com.example.moneykeeper.domain.model.Expense
import com.example.moneykeeper.presenter.utils.Constants
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface ExpenseDao {
    @Query("Select * from ${Constants.EXPENSE}")
    fun getExpenses(): Flow<List<Expense>>

    @Query("Select * from ${Constants.EXPENSE} where ${Constants.EXPENSE_ID} = :id")
    suspend fun getExpenseById(id: Int): Expense

    @Query("SELECT * FROM ${Constants.EXPENSE} WHERE substr(${Constants.EXPENSE_DATE}, 4, 10) = :monthYear")
    fun getExpensesForYearAndMonth(monthYear: String): Flow<List<Expense>>

    @Query("SELECT* FROM tb_expense WHERE substr(exp_date,7,10) = :year")
    fun getExpensesForYear(year: String): Flow<List<Expense>>

    @Query("SELECT * FROM ${Constants.EXPENSE} WHERE ${Constants.EXPENSE_DATE} = :day")
    fun getExpensesForDay(day: String): Flow<List<Expense>>



    @Query("SELECT * FROM ${Constants.EXPENSE} ORDER BY SUBSTR(${Constants.EXPENSE_DATE}, 7) || SUBSTR(${Constants.EXPENSE_DATE}, 4, 2) || SUBSTR(${Constants.EXPENSE_DATE}, 1, 2) DESC LIMIT 5")
    fun get5Expense() : Flow<List<Expense>>

    @Insert
    suspend fun insert(expense: Expense?)

    @Delete
    suspend fun delete(expense: Expense?)

    @Update
    suspend fun update(expense: Expense?)
}