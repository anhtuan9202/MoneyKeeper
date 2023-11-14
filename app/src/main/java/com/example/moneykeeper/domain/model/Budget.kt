package com.example.moneykeeper.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.moneykeeper.presenter.utils.Constants
import java.util.Date

@Entity(tableName = Constants.BUDGET)

data class Budget(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = Constants.BUDGET_ID)
    var budId: Int = 0,
    @ColumnInfo(name = Constants.BUDGET_MONEY)
    val budMoney: String,
    @ColumnInfo(name = Constants.BUDGET_MONTH)
    val budMonth: Date,
    @ColumnInfo(name = Constants.BUDGET_CATEGORY)
    val budCategory: Int,

)
