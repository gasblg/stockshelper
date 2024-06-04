package com.github.gasblg.stockshelper.preferences

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserDataRepository @Inject constructor(
    private val preferencesDataSource: PreferencesDataSource
) : UserDataRepositoryImpl {

    override val userData: Flow<Preferences> =
        preferencesDataSource.userData

    override suspend fun updateLang(lang: String) =
        preferencesDataSource.updateLang(lang)

    override suspend fun updateTheme(theme: String) {
        preferencesDataSource.updateTheme(theme)
    }

    override suspend fun orderBy(sortColumn: String, sortOrder: String) {
        preferencesDataSource.orderBy(sortColumn, sortOrder)
    }

}
