package com.github.gasblg.stockshelper.network.responses

import com.github.gasblg.stockshelper.models.SearchModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchResponse(
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
    val market: String
) {

    fun toModel() = SearchModel(
        ticker = ticker,
        name = name,
        shortName = shortName,
        description = description,
        type = type,
        market = market
    )
}
