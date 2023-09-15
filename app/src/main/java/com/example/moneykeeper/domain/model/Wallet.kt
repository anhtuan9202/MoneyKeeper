package com.example.moneykeeper.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.moneykeeper.utils.Constants

@Entity(tableName = Constants.WALLET)
data class Wallet(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = Constants.WALLET_ID)
    var walId: Int = 0,
    @ColumnInfo(name = Constants.WALLET_IMAGE)
    val walImage: String,
    @ColumnInfo(name = Constants.WALLET_NAME)
    val walName: String,
    @ColumnInfo(name = Constants.WALLET_MONEY)
    var walMoney: String
)

