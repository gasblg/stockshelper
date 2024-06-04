package com.github.gasblg.stockshelper.data.repository.currency

import com.github.gasblg.stockshelper.network.AwaitResponse
import com.github.gasblg.stockshelper.network.api.ApiExchange
import javax.inject.Inject

class CurrencyRepositoryImpl @Inject constructor(
    private val api: ApiExchange
) : CurrencyRepository {

    override suspend fun getCurrencies(lang: String) =
        AwaitResponse.of {
            api.getCurrencies(lang).let { response ->
                response.map { it.toModel() }
            }
        }


    override suspend fun getCurrency(ticker: String, lang: String) =
        AwaitResponse.of {
            api.getCurrency(ticker, lang).toModel()
        }
}