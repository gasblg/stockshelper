package com.github.gasblg.stockshelper.data.utils.date


interface DateConverter {

    fun getDay(originalDate: String): String

    fun getDayOrYesterday(originalDate: String): String

 }