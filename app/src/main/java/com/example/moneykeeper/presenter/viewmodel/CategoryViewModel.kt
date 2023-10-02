package com.example.moneykeeper.presenter.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moneykeeper.domain.model.Category
import com.example.moneykeeper.domain.model.Expense
import com.example.moneykeeper.domain.use_cases.category.CategoryUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject  constructor(
    private val categoryUseCases: CategoryUseCases
):ViewModel()
{

    val categoriesLiveData = MutableLiveData<List<Category>>()
    val expensesLiveData = MutableLiveData<List<Category>>()
    val revenuesLiveData = MutableLiveData<List<Category>>()
    fun getCategories() = viewModelScope.launch(Dispatchers.IO) {
        categoryUseCases.getCategories().collect{
            categoriesLiveData.postValue(it)
        }
    }

    fun getExpenses() = viewModelScope.launch(Dispatchers.IO) {
        categoryUseCases.getExpenses().collect{
            expensesLiveData.postValue(it)
        }


    }
    fun getRevenues() = viewModelScope.launch(Dispatchers.IO) {
        categoryUseCases.getRevenues().collect{
            revenuesLiveData.postValue(it)
        }
    }

    suspend fun getCategoryById(id: Int): Category {
        return withContext(Dispatchers.IO) {
            categoryUseCases.getCategoryById(id)
        }
    }
    fun insertCategory(category: Category){
        viewModelScope.launch(Dispatchers.IO) {
           categoryUseCases.insertCategory(category)
        }
    }
    fun deleteCategory(category: Category){
        viewModelScope.launch(Dispatchers.IO) {
            categoryUseCases.deleteCategory(category)
        }
    }




}