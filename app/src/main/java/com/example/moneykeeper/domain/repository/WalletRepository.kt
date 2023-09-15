package com.example.moneykeeper.domain.repository

import com.example.moneykeeper.domain.model.Expense
import com.example.moneykeeper.domain.model.Wallet
import kotlinx.coroutines.flow.Flow

interface WalletRepository {

    fun getWallets(): Flow<List<Wallet>>

    suspend fun insert(wallet: Wallet)

    suspend fun update(wallet: Wallet)
    suspend fun delete(wallet: Wallet)

    suspend fun getWalletById(id: Int): Wallet
}