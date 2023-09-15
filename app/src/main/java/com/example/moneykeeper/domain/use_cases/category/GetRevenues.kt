package com.example.moneykeeper.domain.use_cases.category

import com.example.moneykeeper.domain.repository.CategoryRepository

class GetRevenues(private val repository: CategoryRepository) {

    operator fun invoke() = repository.getRevenues()
}