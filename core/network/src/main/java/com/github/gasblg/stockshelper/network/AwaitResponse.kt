package com.github.gasblg.stockshelper.network

import kotlinx.coroutines.*
import com.github.gasblg.stockshelper.common.Result
import com.github.gasblg.stockshelper.common.ext.asError
import com.github.gasblg.stockshelper.common.ext.asResult

class AwaitResponse {

    companion object {

        suspend fun <T> of(deferred: suspend () -> T): Result<T> = withContext(Dispatchers.IO) {
            try {
                AwaitResponse().send(deferred).asResult()
            } catch (e: Exception) {
                e.asError()
            }
        }
    }

    private suspend fun <T> send(
        block: suspend () -> T,
    ): T {
        return try {
            block()
        } catch (e: Exception) {
            throw e
        }
    }

}