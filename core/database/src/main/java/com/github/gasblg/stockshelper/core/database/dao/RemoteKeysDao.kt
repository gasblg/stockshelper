package com.github.gasblg.stockshelper.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.github.gasblg.stockshelper.core.database.entities.RemoteKeyEntity

@Dao
interface RemoteKeysDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRemoteKey(remoteKeyEntity: RemoteKeyEntity)

    @Query("SELECT * FROM keys WHERE label = :label")
    suspend fun getRemoteKeyByLabel(label: String): RemoteKeyEntity

    @Query("DELETE FROM keys WHERE label = :label")
    suspend fun deleteRemoteKeyByLabel(label: String)
}