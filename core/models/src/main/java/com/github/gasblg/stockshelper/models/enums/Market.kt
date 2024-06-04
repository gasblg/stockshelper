package com.github.gasblg.stockshelper.models.enums

import com.github.gasblg.stockshelper.models.R

enum class Market(val value: String) {
    STOCK("stock"),
    CURRENCY("currency"),
    DERIVATIVES("derivatives");

    companion object {
        fun getName(market: String): Int {
            return when (market) {
                STOCK.value -> R.string.core_models_stock
                CURRENCY.value -> R.string.core_models_currency
                else -> R.string.core_models_derivatives
            }
        }
    }
}