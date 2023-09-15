package com.example.moneykeeper.domain.use_cases.wallet

import com.example.moneykeeper.domain.model.Wallet
import com.example.moneykeeper.domain.repository.WalletRepository


class DeleteWallet(private val repository: WalletRepository) {

    suspend operator fun invoke(wallet: Wallet) = repository.delete(wallet)
}