package com.example.moneykeeper.utils

import android.util.Log
import androidx.room.TypeConverter
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class Converter {
    private val DATE_FORMAT = SimpleDateFormat("dd/MM/yyyy")
    @TypeConverter
    @Throws(ParseException::class)
    fun toDate(value: String?): Date? {
        return try {
            if (value == null) {
                null
            } else {
                DATE_FORMAT.parse(value)
            }
        } catch (e: ParseException) {
            Log.e("ERROR", "toDate: error")
            throw e
        }
    }

    @TypeConverter
    fun ToString(date: Date?): String? {
        return if (date == null) {
            null
        } else DATE_FORMAT.format(date)
    }
}