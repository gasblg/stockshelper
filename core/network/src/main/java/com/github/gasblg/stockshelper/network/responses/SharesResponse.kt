package com.github.gasblg.stockshelper.network.responses

import com.github.gasblg.stockshelper.core.database.entities.CandleEntity
import com.github.gasblg.stockshelper.core.database.entities.CandleList
import com.github.gasblg.stockshelper.core.database.entities.FavoritesEntity
import com.github.gasblg.stockshelper.core.database.entities.SharesEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SharesResponse(
    @SerialName("ticker")
    val ticker: String,
    @SerialName("date")
    val date: String,
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
    @SerialName("price")
    val price: Double,
    @SerialName("open")
    val open: Double,
    @SerialName("low")
    val low: Double,
    @SerialName("high")
    val high: Double,
    @SerialName("close")
    val close: Double,
    @SerialName("volume")
    val volume: Int,
    @SerialName("percent")
    val percent: Double,
    @SerialName("candles")
    val candles: List<CandleResponse>? = null
) {
    fun toEntity() = SharesEntity(
        ticker = ticker,
        date = date,
        name = name,
        shortName = shortName,
        description = description,
        type = type,
        market = market,
        price = price,
        open = open,
        low = low,
        high = high,
        close = close,
        volume = volume,
        percent = percent,
        candles = CandleList(candles?.map { it.toCandles() })
    )

    fun toFavoritesEntity() = FavoritesEntity(
        ticker = ticker,
        date = date,
        name = name,
        shortName = shortName,
        description = description,
        type = type,
        market = market,
        price = price,
        open = open,
        low = low,
        high = high,
        close = close,
        volume = volume,
        percent = percent,
        candles = CandleList(candles?.map { it.toCandles() })
    )

    private fun CandleResponse.toCandles() = run {
        CandleEntity(
            date = date,
            value = price,
            open = open,
            low = low,
            high = high,
            close = close,
            volume = volume,
            percent = percent
        )
    }

}