package com.example.moneykeeper.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.moneykeeper.presenter.utils.Constants

@Entity(tableName = Constants.CATEGORY)
data class Category(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = Constants.CATEGORY_ID)
    var cateId: Int = 0,
    @ColumnInfo(name = Constants.CATEGORY_NAME)
    val cateName: String,
    @ColumnInfo(name = Constants.CATEGORY_IMAGE)
    val cateImage: String,
    @ColumnInfo(name = Constants.CATEGORY_TYPE)
    val cateType: Int
)

