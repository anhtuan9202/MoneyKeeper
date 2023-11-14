package com.example.moneykeeper.domain.model


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.moneykeeper.presenter.utils.Constants
import java.util.*

@Entity(tableName = Constants.EXPENSE)
data class Expense(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = Constants.EXPENSE_ID)
    var expId: Int = 0,
    @ColumnInfo(name = Constants.EXPENSE_NOTE)
    val expNote: String,
    @ColumnInfo(name = Constants.EXPENSE_MONEY)
    val expMoney: String,
    @ColumnInfo(name = Constants.EXPENSE_DATE)
    val expDate: Date,
    @ColumnInfo(name = Constants.EXPENSE_TYPE)
    val expType: Int,
    @ColumnInfo(name = Constants.EXPENSE_CATEGORY)
    val expCategory: Int,
    @ColumnInfo(name = Constants.EXPENSE_WALLET)
    val expWallet: Int,
)

