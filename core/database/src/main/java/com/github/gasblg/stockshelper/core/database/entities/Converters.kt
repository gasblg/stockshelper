package com.github.gasblg.stockshelper.core.database.entities

import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class Converters {
    @TypeConverter
    fun fromList(value: CandleList) = Json.encodeToString(value)

    @TypeConverter
    fun toList(value: String) = Json.decodeFromString<CandleList>(value)
}