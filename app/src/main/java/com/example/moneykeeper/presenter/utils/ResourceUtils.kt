package com.example.moneykeeper.presenter.utils

import android.content.Context

object ResourceUtils {
    fun Context.getDrawableResourceId(name: String): Int {
        return resources.getIdentifier(name, "drawable", packageName)
    }
}