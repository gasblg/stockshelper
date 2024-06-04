package com.github.gasblg.stockshelper.models


data class SharesModel(
    val ticker: String,
    val date: String,
    val name: String,
    val shortName: String,
    val description: String,
    val type: String,
    val market: String,
    val price: Double,
    val open: Double,
    val low: Double,
    val high: Double,
    val close: Double,
    val volume: Int,
    val percent: Double,
    val candles: List<CandleModel>? = null
)