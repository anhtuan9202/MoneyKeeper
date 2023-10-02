package com.example.moneykeeper.utils

import java.text.DecimalFormat
import java.text.NumberFormat
import javax.inject.Inject

object NumberFormatter {



    fun formatNumber(string: String): String {
        val formatter: DecimalFormat = NumberFormat.getInstance() as DecimalFormat
        formatter.applyPattern("#,###,###,###")
        val number = string.toLongOrNull() ?: 0
        return formatter.format(number)
    }
}