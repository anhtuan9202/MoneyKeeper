package com.example.moneykeeper.presenter.utils

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    private const val DEFAULT_DATE_FORMAT = "dd/MM/yyyy"

    fun formatDate(date: Date, pattern: String = DEFAULT_DATE_FORMAT): String {
        val sdf = SimpleDateFormat(pattern, Locale.getDefault())
        return sdf.format(date)
    }
}