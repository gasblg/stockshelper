package com.github.gasblg.stockshelper.preferences

import android.util.Log
import androidx.datastore.core.DataStore
import com.github.gasblg.stockshelper.models.enums.Language
import com.github.gasblg.stockshelper.models.enums.SortColumn
import com.github.gasblg.stockshelper.models.enums.SortOrder
import com.github.gasblg.stockshelper.models.enums.Theme
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject


class PreferencesDataSource @Inject constructor(
    private val preferences: DataStore<UserPreferences>,
) {
    val userData = preferences.data
        .map {
            Preferences(
                sortColumn = if (it.sortColumn.isNullOrEmpty()) SortColumn.PERCENT.type else it.sortColumn,
                sortOrder = if (it.sortOrder.isNullOrEmpty()) SortOrder.DESC.type else it.sortOrder,
                lang = if (it.lang.isNullOrEmpty()) Language.EN.type else it.lang,
                theme = if (it.theme.isNullOrEmpty()) Theme.DARK.type else it.theme
            )
        }

    suspend fun updateLang(lang: String) {
        try {
            preferences.updateData {
                it.copy {
                    this.lang = lang
                }
            }
        } catch (ioException: IOException) {
            Log.e("Preferences", "Failed to update user preferences", ioException)
        }
    }


    suspend fun updateTheme(theme: String) {
        try {
            preferences.updateData {
                it.copy {
                    this.theme = theme
                }

            }
        } catch (ioException: IOException) {
            Log.e("Preferences", "Failed to update user preferences", ioException)
        }
    }

    suspend fun orderBy(sortColumn: String, sortOrder: String) {
        try {
            preferences.updateData {
                it.copy {
                    this.sortColumn = sortColumn
                    this.sortOrder = sortOrder
                }
            }
        } catch (ioException: IOException) {
            Log.e("Preferences", "Failed to update user preferences", ioException)
        }
    }


}


