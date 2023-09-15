package com.example.moneykeeper.domain.use_cases.wallet

import com.example.moneykeeper.domain.model.Expense
import com.example.moneykeeper.domain.model.Wallet
import com.example.moneykeeper.domain.repository.ExpenseRepository
import com.example.moneykeeper.domain.repository.WalletRepository


class UpdateWallet(private val repository: WalletRepository) {

    suspend operator fun invoke(wallet: Wallet) = repository.update(wallet)
}