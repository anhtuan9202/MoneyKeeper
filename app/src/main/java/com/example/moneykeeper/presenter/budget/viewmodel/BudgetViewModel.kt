package com.example.moneykeeper.presenter.budget.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moneykeeper.domain.model.Budget
import com.example.moneykeeper.domain.model.Expense
import com.example.moneykeeper.domain.use_cases.budget.BudgetUseCases
import com.example.moneykeeper.domain.use_cases.expense.ExpenseUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject
@HiltViewModel
class BudgetViewModel @Inject constructor(
    private val budgetUseCases: BudgetUseCases
) : ViewModel(){
    val budgetsLiveData = MutableLiveData<List<Budget>>()
    val budget5LiveData = MutableLiveData<List<Budget>>()
    val budgetsMonthYearLiveData = MutableLiveData<List<Budget>>()

    fun getBudgets() = viewModelScope.launch(Dispatchers.IO) {
        budgetUseCases.getBudgets().collect{
            budgetsLiveData.postValue(it)
        }
    }
    fun get5Budget() = viewModelScope.launch(Dispatchers.IO) {
        budgetUseCases.get5Budget().collect{
            budget5LiveData.postValue(it)
        }
    }
    fun getBudgetForMonthAndYear(monthYear: String) = viewModelScope.launch(Dispatchers.IO) {
        budgetUseCases.getBudgetForMonthAndYear(monthYear).collect{
            budgetsMonthYearLiveData.postValue(it)
        }
    }




    fun insertBudget(budget: Budget){
        viewModelScope.launch(Dispatchers.IO) {
            budgetUseCases.insertBudget(budget)
        }
    }
    fun updateBudget(budget: Budget){
        viewModelScope.launch(Dispatchers.IO) {
            budgetUseCases.updateBudget(budget)
        }
    }
    fun deleteBudget(budget: Budget){
        viewModelScope.launch(Dispatchers.IO) {
            budgetUseCases.deleteBudget(budget)
        }
    }


}