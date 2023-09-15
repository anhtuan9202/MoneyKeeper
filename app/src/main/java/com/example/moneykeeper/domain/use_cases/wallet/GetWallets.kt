package com.example.moneykeeper.domain.use_cases.wallet

import com.example.moneykeeper.domain.repository.WalletRepository


class GetWallets(private val repository: WalletRepository) {

    operator fun invoke() = repository.getWallets()
}