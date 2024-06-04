package com.github.gasblg.stockshelper.core.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "candle")
data class CandleEntity(
    @PrimaryKey(autoGenerate = false)
    val date: String,
    val value: Float,
    val open: Float,
    val low: Float,
    val high: Float,
    val close: Float,
    val volume: Int,
    val percent: Float
)