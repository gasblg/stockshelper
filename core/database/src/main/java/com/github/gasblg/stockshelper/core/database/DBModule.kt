package com.github.gasblg.stockshelper.core.database

import android.app.Application
import com.github.gasblg.stockshelper.core.database.db.AppDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DBModule {

    @Provides
    @Singleton
    fun providesDB(app: Application): AppDataBase {
        return AppDataBase.invoke(app.applicationContext)
    }

}
