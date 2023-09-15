package com.example.moneykeeper.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.moneykeeper.domain.model.Category
import com.example.moneykeeper.domain.model.Expense
import com.example.moneykeeper.domain.model.MoneyType
import com.example.moneykeeper.domain.model.Wallet
import com.example.moneykeeper.utils.Converter

@Database(
    entities = [Category::class, Expense::class, MoneyType::class, Wallet::class],
    version = 2
)
@TypeConverters(Converter::class)
abstract class Database: RoomDatabase() {
    abstract val categoryDao: CategoryDao
    abstract val expenseDao: ExpenseDao
    abstract val moneyTypeDao: MoneyTypeDao
    abstract val walletDao: WalletDao

    companion object{
        const val DATABASE_NAME = "MoneyKeeper.db"
    }
}