package com.github.gasblg.stockshelper.models


data class SearchModel(
    val ticker: String,
    val name: String,
    val shortName: String,
    val description: String,
    val type: String,
    val market: String
)
