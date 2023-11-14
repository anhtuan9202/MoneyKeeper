package com.example.moneykeeper.presenter.utils

import android.content.Context
import android.content.SharedPreferences

object PrefsUtils {
    private lateinit var sharedPreferences: SharedPreferences
    private const val PREFERENCES_NAME = "MoneyKeeperPreferences"

    fun initialize(context: Context) {
        sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
    }



    fun putInt(key: String, value: Int){
        val editor = sharedPreferences.edit()
        editor.putInt(key,value)
        editor.apply()
    }
    fun getInt(key:String, defaultValue: Int): Int {
        return sharedPreferences.getInt(key,defaultValue)
    }
    fun putLong(key: String, value: Long){
        val editor = sharedPreferences.edit()
        editor.putLong(key,value)
        editor.apply()
    }
    fun getLong(key:String, defaultValue: Long): Long {
        return sharedPreferences.getLong(key,defaultValue)
    }




    fun putBoolean(key: String, value: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean {
        return sharedPreferences.getBoolean(key, defaultValue)
    }

    fun putString(key: String, value: String?) {
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getString(key: String, defaultValue: String? = null): String? {
        return sharedPreferences.getString(key, defaultValue)
    }


    fun clear() {
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }
}