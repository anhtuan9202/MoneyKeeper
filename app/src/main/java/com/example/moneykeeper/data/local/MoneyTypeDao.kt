package com.example.moneykeeper.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.moneykeeper.domain.model.MoneyType
import com.example.moneykeeper.presenter.utils.Constants

@Dao
interface MoneyTypeDao {

    @Query("Select * from ${Constants.MONEY_TYPE} where ${Constants.TYPE_ID} = 1")
    suspend fun getRevenues(): MoneyType

    @Query("Select * from ${Constants.MONEY_TYPE} where ${Constants.TYPE_ID} = 2")
    suspend fun getExpenses(): MoneyType

    @Insert
    suspend fun insert(moneyType: MoneyType)

    @Delete
    suspend fun delete(moneyType: MoneyType)

    @Update
    suspend fun update(moneyType: MoneyType)
}