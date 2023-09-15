package com.example.moneykeeper.data.repository

import com.example.moneykeeper.data.local.WalletDao
import com.example.moneykeeper.domain.model.Wallet
import com.example.moneykeeper.domain.repository.WalletRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WalletRepositoryImpl @Inject constructor(
    private val dao: WalletDao
): WalletRepository {
    override fun getWallets(): Flow<List<Wallet>> {
        return dao.getWallets()
    }

    override suspend fun insert(wallet: Wallet) {
        return dao.insert(wallet)
    }

    override suspend fun update(wallet: Wallet) {
        return dao.update(wallet)
    }

    override suspend fun delete(wallet: Wallet) {
        return dao.delete(wallet)
    }

    override suspend fun getWalletById(id: Int): Wallet {
        return dao.getWalletById(id)
    }


}