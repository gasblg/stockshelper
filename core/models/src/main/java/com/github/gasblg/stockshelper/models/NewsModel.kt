package com.github.gasblg.stockshelper.models

data class NewsModel(
    val id: Int,
    val title: String,
    val date: String,
    val body: String? = null
)