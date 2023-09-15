package com.example.moneykeeper.presenter.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moneykeeper.domain.model.Expense
import com.example.moneykeeper.domain.use_cases.expense.ExpenseUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject
@HiltViewModel
class ExpenseViewModel @Inject constructor(
    private val expenseUseCases: ExpenseUseCases
) : ViewModel(){
    val expensesLiveData = MutableLiveData<List<Expense>>()
    val expense5LiveData = MutableLiveData<List<Expense>>()
    val expensesMonthLiveData = MutableLiveData<List<Expense>>()
    val expensesDayLiveData = MutableLiveData<List<Expense>>()
    val expensesYearLiveData = MutableLiveData<List<Expense>>()
    fun getExpenses() = viewModelScope.launch(Dispatchers.IO) {
        expenseUseCases.getExpenses().collect{
            expensesLiveData.postValue(it)
        }
    }

    fun get5Expense() = viewModelScope.launch(Dispatchers.IO) {
        expenseUseCases.get5Expenses().collect{
            expense5LiveData.postValue(it)
        }
    }

    fun getExpensesForMonth(monthYear: String) = viewModelScope.launch(Dispatchers.IO) {
        expenseUseCases.getExpensesForMonth(monthYear).collect{
            expensesMonthLiveData.postValue(it)
        }
    }

    fun getExpensesForDay(day: String) = viewModelScope.launch(Dispatchers.IO) {
        expenseUseCases.getExpensesForDay(day).collect{
            expensesDayLiveData.postValue(it)
        }
    }

    fun getExpensesForYear(year: String) = viewModelScope.launch(Dispatchers.IO) {
        expenseUseCases.getExpensesForYear(year).collect{
            expensesYearLiveData.postValue(it)
        }
    }



    fun insertExpense(expense: Expense){
        viewModelScope.launch(Dispatchers.IO) {
            expenseUseCases.insertExpense(expense)
        }
    }
    fun deleteExpense(expense: Expense){
        viewModelScope.launch(Dispatchers.IO) {
            expenseUseCases.deleteExpense(expense)
        }
    }
    fun updateExpense(expense: Expense){
        viewModelScope.launch(Dispatchers.IO) {
            expenseUseCases.updateExpense(expense)
        }
    }

}