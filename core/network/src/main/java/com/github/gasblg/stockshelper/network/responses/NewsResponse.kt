package com.github.gasblg.stockshelper.network.responses

import com.github.gasblg.stockshelper.models.NewsModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NewsResponse(
    @SerialName("id")
    val id: Int,
    @SerialName("title")
    val title: String,
    @SerialName("date")
    val date: String,
    @SerialName("body")
    val body: String? = null
) {
    fun toModel() = NewsModel(
        id = id,
        title = title,
        date = date,
        body = body
    )

}