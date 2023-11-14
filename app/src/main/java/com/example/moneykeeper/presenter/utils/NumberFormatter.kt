package com.example.moneykeeper.presenter.utils

import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Locale
import javax.inject.Inject

object NumberFormatter {



    fun formatNumber(string: String): String {
        val formatter: DecimalFormat = NumberFormat.getInstance(Locale.US) as DecimalFormat
        formatter.applyPattern("#,###,###,###")
        val number = string.toLongOrNull() ?: 0
        return formatter.format(number)
    }
}