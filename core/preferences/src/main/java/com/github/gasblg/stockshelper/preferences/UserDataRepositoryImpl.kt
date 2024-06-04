package com.github.gasblg.stockshelper.preferences

import kotlinx.coroutines.flow.Flow

interface UserDataRepositoryImpl {

    val userData: Flow<Preferences>

    suspend fun updateLang(lang: String)

    suspend fun updateTheme(theme: String)

    suspend fun orderBy(sortColumn: String, sortOrder: String)

}
