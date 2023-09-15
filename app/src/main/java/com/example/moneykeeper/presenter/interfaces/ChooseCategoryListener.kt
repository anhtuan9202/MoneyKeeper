package com.example.moneykeeper.presenter.interfaces

import com.example.moneykeeper.domain.model.Category

interface ChooseCategoryListener {

    fun onChooseListener(category: Category)
}