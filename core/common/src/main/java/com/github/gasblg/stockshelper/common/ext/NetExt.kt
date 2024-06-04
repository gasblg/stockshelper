package com.github.gasblg.stockshelper.common.ext

import com.github.gasblg.stockshelper.common.Result


fun <T> T.asResult(): Result<T> = Result.Data(this)

fun <T> Throwable.asError(): Result<T> = Result.Error(this)

