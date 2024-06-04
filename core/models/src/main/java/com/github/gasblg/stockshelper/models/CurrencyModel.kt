package com.github.gasblg.stockshelper.models

import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

data class CurrencyModel(
    val ticker: String,
    val name: String,
    val shortName: String,
    val description: String,
    val type: String,
    val market: String,
    val date: String,
    val rate: Double,
    val percent: Double
) {
    private val formatSymbols = DecimalFormatSymbols(Locale.ENGLISH).apply {
        decimalSeparator = '.'
        groupingSeparator = ' '
    }

    fun roundedRate(): Double? = rate.apply {
        val df = DecimalFormat("#.##", formatSymbols)
        df.roundingMode = RoundingMode.FLOOR
        val number = df.format(this)
        return number.toDoubleOrNull()
    }

}

