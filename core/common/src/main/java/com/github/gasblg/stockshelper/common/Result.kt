package com.github.gasblg.stockshelper.common

sealed class Result<out T> {

    data class Data<out T>(
        val data: T
    ) : Result<T>()

    data class Error<out T>(
        val error: Throwable
    ) : Result<T>()

}