package com.github.gasblg.stockshelper.network.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CandleResponse(
    @SerialName("date")
    val date: String,
    @SerialName("price")
    val price: Float,
    @SerialName("open")
    val open: Float,
    @SerialName("low")
    val low: Float,
    @SerialName("high")
    val high: Float,
    @SerialName("close")
    val close: Float,
    @SerialName("volume")
    val volume: Int,
    @SerialName("percent")
    val percent: Float
)
