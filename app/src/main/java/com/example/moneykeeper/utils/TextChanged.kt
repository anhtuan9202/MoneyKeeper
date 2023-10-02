package com.example.moneykeeper.utils

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

object TextChanged {

    fun onTextChangedListener(et :EditText): TextWatcher? {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                et.removeTextChangedListener(this)
                try {
                    var originalString = s.toString()
                    if (originalString.contains(",")) {
                        originalString = originalString.replace(",".toRegex(), "")
                    }
                    val longval: Long = originalString.toLong()
                    val formatter = NumberFormat.getInstance(Locale.US) as DecimalFormat
                    formatter.applyPattern("#,###,###,###")
                    val formattedString = formatter.format(longval)

                    et.setText(formattedString)
                    et.text?.let { et.setSelection(it.length) }
                } catch (nfe: NumberFormatException) {
                    nfe.printStackTrace()
                }
                et.addTextChangedListener(this)
            }
        }
    }
}