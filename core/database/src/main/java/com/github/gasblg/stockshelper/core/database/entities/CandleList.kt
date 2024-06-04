package com.github.gasblg.stockshelper.core.database.entities

import kotlinx.serialization.Serializable

@Serializable
data class CandleList(
    val candlesList: List<CandleEntity>?
)