package com.github.gasblg.stockshelper.network.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PaginationResponse<T>(
    @SerialName("count")
    val count: Long? = null,
    @SerialName("next")
    val next: Int? = null,
    @SerialName("previous")
    val previous: Int? = null,
    @SerialName("results")
    val results: List<T> = listOf()
)