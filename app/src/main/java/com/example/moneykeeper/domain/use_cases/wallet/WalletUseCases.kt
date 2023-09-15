package com.example.moneykeeper.domain.use_cases.wallet

data class WalletUseCases (
    val getWallets: GetWallets,
    val updateWallet: UpdateWallet,
    val getWalletById: GetWalletById,
    val insertWallet: InsertWallet,
    val deleteWallet: DeleteWallet
)