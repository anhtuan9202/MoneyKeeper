package com.example.moneykeeper.presenter.interfaces

import com.example.moneykeeper.domain.model.Wallet

interface ChooseWalletListener {

    fun onChooseListener(wallet: Wallet)
}