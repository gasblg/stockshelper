package com.github.gasblg.stockshelper.models.enums

import com.github.gasblg.stockshelper.models.R

enum class Type(val value: String) {
    SHARES("shares"),
    CURRENCY("currency"),
    FUTURES("futures"),
    OPTIONS("options");


    companion object{
        fun getType(type: String): Int {
            return when (type) {
                SHARES.value -> R.string.core_models_shares
                CURRENCY.value -> R.string.core_models_currency
                FUTURES.value -> R.string.core_models_futures
                else -> R.string.core_models_options
            }
        }
    }
}