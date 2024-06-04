package com.github.gasblg.stockshelper.network.responses

import com.github.gasblg.stockshelper.models.DerivativesModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DerivativesResponse(
    @SerialName("ticker")
    val ticker: String,
    @SerialName("name")
    val name: String,
    @SerialName("short_name")
    val shortName: String,
    @SerialName("description")
    val description: String,
    @SerialName("type")
    val type: String,
    @SerialName("market")
    val market: String,
    @SerialName("first_trade")
    val firstTrade: String,
    @SerialName("last_trade")
    val lastTrade: String,
    @SerialName("date")
    val date: String,
    @SerialName("contract_type")
    val contractType: String
) {
    fun toModel() = DerivativesModel(
        ticker = ticker,
        name = name,
        shortName = shortName,
        description = description,
        type = type,
        market = market,
        firstTrade = firstTrade,
        lastTrade = lastTrade,
        date = date,
        contractType = contractType
    )
}