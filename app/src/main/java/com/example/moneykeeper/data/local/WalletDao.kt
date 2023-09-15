package com.example.moneykeeper.data.local

import androidx.room.*
import com.example.moneykeeper.domain.model.Wallet
import com.example.moneykeeper.utils.Constants
import kotlinx.coroutines.flow.Flow

@Dao
interface WalletDao {
    @Query("SELECT * FROM ${Constants.WALLET}")
    fun getWallets(): Flow<List<Wallet>> // Correct return type

    @Query("SELECT * FROM ${Constants.WALLET} WHERE ${Constants.WALLET_ID} = :id")
    suspend fun getWalletById(id: Int): Wallet // Correct return type

    @Insert
    suspend fun insert(wallet: Wallet) // Correct parameter type

    @Delete
    suspend fun delete(wallet: Wallet) // Correct parameter type

    @Update
    suspend fun update(wallet: Wallet) // Correct parameter type
}
