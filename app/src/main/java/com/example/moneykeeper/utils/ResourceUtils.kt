package com.example.moneykeeper.utils

import android.content.Context

object ResourceUtils {
    fun Context.getDrawableResourceId(name: String): Int {
        return resources.getIdentifier(name, "drawable", packageName)
    }
}