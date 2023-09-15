package com.example.moneykeeper.domain.use_cases.wallet

import com.example.moneykeeper.domain.repository.CategoryRepository
import com.example.moneykeeper.domain.repository.WalletRepository

class GetWalletById(private val category: WalletRepository) {
    suspend operator fun invoke(id: Int) = category.getWalletById(id)
}