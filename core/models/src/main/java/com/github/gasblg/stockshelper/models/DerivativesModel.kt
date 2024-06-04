package com.github.gasblg.stockshelper.models


data class DerivativesModel(
    val ticker: String,
    val name: String,
    val shortName: String,
    val description: String,
    val type: String,
    val market: String,
    val firstTrade: String,
    val lastTrade: String,
    val date: String,
    val contractType: String
)