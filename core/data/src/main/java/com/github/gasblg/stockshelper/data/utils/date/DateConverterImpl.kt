package com.github.gasblg.stockshelper.data.utils.date

import android.content.Context
import com.github.gasblg.stockshelper.data.R
import dagger.hilt.android.qualifiers.ApplicationContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject


class DateConverterImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : DateConverter {

    private val originalFormat = SimpleDateFormat("yyyy-MM-dd", Locale("en"))
    private val shortFormat = SimpleDateFormat("dd.MM", Locale.getDefault())

    override fun getDay(originalDate: String): String {
        return try {
            val date = originalFormat.parse(originalDate)
            if (date != null) shortFormat.format(date) else ""
        } catch (e: Exception) {
            ""
        }
    }

    override fun getDayOrYesterday(originalDate: String): String {
        return try {
            val date = originalFormat.parse(originalDate)
            val yesterday = shortFormat.format(getYesterday())
            val today = shortFormat.format(Date())

            when (val day = if (date != null) shortFormat.format(date) else "") {
                yesterday -> context.getString(R.string.core_data_yesterday)
                today -> context.getString(R.string.core_data_today)
                else -> day
            }
        } catch (e: Exception) {
            ""
        }
    }

    private fun getYesterday(): Date {
        val cal = Calendar.getInstance()
        cal.add(Calendar.DATE, -1)
        return cal.time
    }

}