package com.example.moneykeeper.presenter.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moneykeeper.domain.model.Category
import com.example.moneykeeper.domain.model.Expense
import com.example.moneykeeper.domain.model.Wallet
import com.example.moneykeeper.domain.use_cases.expense.ExpenseUseCases
import com.example.moneykeeper.domain.use_cases.wallet.WalletUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
@HiltViewModel
class WalletViewModel @Inject constructor(
    private val walletUseCases: WalletUseCases
) : ViewModel(){
    val walletsLiveData = MutableLiveData<List<Wallet>>()
    fun getWallets() = viewModelScope.launch(Dispatchers.IO) {
        walletUseCases.getWallets().collect{
            walletsLiveData.postValue(it)
        }
    }

    fun updateWallet(wallet: Wallet){
        viewModelScope.launch(Dispatchers.IO) {
            walletUseCases.updateWallet(wallet)
        }
    }

    fun insertWallet(wallet: Wallet){
        viewModelScope.launch(Dispatchers.IO) {
            walletUseCases.insertWallet(wallet)
        }
    }
    fun deleteWallet(wallet: Wallet){
        viewModelScope.launch(Dispatchers.IO) {
            walletUseCases.deleteWallet(wallet)
        }
    }



    suspend fun getWalletById(id: Int): Wallet {
        return withContext(Dispatchers.IO) {
            walletUseCases.getWalletById(id)
        }
    }

}