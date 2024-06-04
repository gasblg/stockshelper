package com.github.gasblg.stockshelper.models

import java.io.Serializable

data class CandleModel(
    val date: String,
    val open: Float,
    val low: Float,
    val high: Float,
    val close: Float,
    val volume: Int,
    val price: Float,
    val percent: Float
) : Serializable
