package com.example.moneykeeper.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.moneykeeper.utils.Constants

@Entity(tableName = Constants.MONEY_TYPE)
data class MoneyType(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = Constants.TYPE_ID)
    var moneyTypeId: Int,
    @ColumnInfo(name = Constants.TYPE_NAME)
    val moneyTypeName: String,
)

