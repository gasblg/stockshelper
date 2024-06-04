package com.github.gasblg.stockshelper.data.repository.currency


import com.github.gasblg.stockshelper.models.CurrencyModel
import com.github.gasblg.stockshelper.common.Result

interface CurrencyRepository {
    suspend fun getCurrencies(lang: String): Result<List<CurrencyModel>>

    suspend fun getCurrency(ticker:String, lang: String): Result<CurrencyModel>


}