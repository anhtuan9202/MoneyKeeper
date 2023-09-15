package com.example.moneykeeper.utils

import java.text.DecimalFormat
import java.text.NumberFormat
import javax.inject.Inject

class NumberFormatter @Inject constructor() {
    private val formatter: DecimalFormat = NumberFormat.getInstance() as DecimalFormat

    init {
        formatter.applyPattern("#,###,###,###")
    }

    fun formatNumber(string: String): String {
        val number = string.toLongOrNull() ?: 0
        return formatter.format(number)
    }
}