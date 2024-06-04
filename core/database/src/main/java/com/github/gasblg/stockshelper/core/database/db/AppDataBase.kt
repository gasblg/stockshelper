package com.github.gasblg.stockshelper.core.database.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.github.gasblg.stockshelper.core.database.dao.FavoritesDao
import com.github.gasblg.stockshelper.core.database.dao.RemoteKeysDao
import com.github.gasblg.stockshelper.core.database.dao.SharesDao
import com.github.gasblg.stockshelper.core.database.entities.CandleEntity
import com.github.gasblg.stockshelper.core.database.entities.Converters
import com.github.gasblg.stockshelper.core.database.entities.FavoritesEntity
import com.github.gasblg.stockshelper.core.database.entities.RemoteKeyEntity
import com.github.gasblg.stockshelper.core.database.entities.SharesEntity

@TypeConverters(Converters::class)
@Database(
    entities = [SharesEntity::class, RemoteKeyEntity::class, FavoritesEntity::class, CandleEntity::class],
    version = 1, exportSchema = false
)
abstract class AppDataBase : RoomDatabase() {

    abstract val sharesDao: SharesDao
    abstract val favoritesDao: FavoritesDao
    abstract val remoteKeysDao: RemoteKeysDao

    companion object {
        private const val DATABASE_NAME = "shares_app_db"

        @Volatile
        private var instance: AppDataBase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance
            ?: synchronized(LOCK) {
                instance
                    ?: buildDatabase(
                        context
                    ).also {
                    instance = it
                }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppDataBase::class.java,
                DATABASE_NAME
            ).fallbackToDestructiveMigration()
                .build()
    }

}
