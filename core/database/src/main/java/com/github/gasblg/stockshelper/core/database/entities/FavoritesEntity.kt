package com.github.gasblg.stockshelper.core.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.github.gasblg.stockshelper.models.CandleModel
import com.github.gasblg.stockshelper.models.SharesModel

@Entity(tableName = "favorites")
data class FavoritesEntity(
    @PrimaryKey(autoGenerate = false)
    val ticker: String,
    val date: String,
    val name: String,
    val shortName: String,
    val description: String,
    val type: String,
    val market: String,
    val open: Double,
    val low: Double,
    val high: Double,
    val close: Double,
    val volume: Int,
    val price: Double,
    val percent: Double,
    val candles: CandleList
){
    fun toModel() = SharesModel(
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
        candles = candles.candlesList?.map { it.toCandles() }
    )

    private fun CandleEntity.toCandles() = run {
        CandleModel(
            date = this.date,
            price = this.value,
            open = this.open,
            low = this.low,
            high = this.high,
            close = this.close,
            volume = this.volume,
            percent = this.percent
        )
    }
}