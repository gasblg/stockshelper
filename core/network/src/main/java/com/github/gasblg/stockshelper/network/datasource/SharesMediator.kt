package com.github.gasblg.stockshelper.network.datasource

import androidx.paging.*
import androidx.room.withTransaction
import com.github.gasblg.stockshelper.core.database.db.AppDataBase
import com.github.gasblg.stockshelper.core.database.entities.RemoteKeyEntity
import com.github.gasblg.stockshelper.core.database.entities.SharesEntity
import com.github.gasblg.stockshelper.network.AwaitResponse
import retrofit2.HttpException
import java.io.IOException
import com.github.gasblg.stockshelper.network.datasource.settings.PageSettings
import com.github.gasblg.stockshelper.common.Result
import com.github.gasblg.stockshelper.network.api.ApiExchange

@OptIn(ExperimentalPagingApi::class)
class SharesMediator(
    private val label: String,
    private val database: AppDataBase,
    private val api: ApiExchange,
    private val sortColumn: String,
    private val sortOrder: String,
    private val lang: String
) : RemoteMediator<Int, SharesEntity>() {


    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }


    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, SharesEntity>
    ): MediatorResult {
        try {

            val page = when (loadType) {
                LoadType.REFRESH -> PageSettings.DEFAULT_START
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val remoteKey = database.withTransaction {
                        database.remoteKeysDao.getRemoteKeyByLabel(label)
                    }

                    if (remoteKey.nextKey == null) {
                        return MediatorResult.Success(endOfPaginationReached = true)
                    }
                    remoteKey.nextKey
                }
            } ?: PageSettings.DEFAULT_START

            when (val result = AwaitResponse.of {
                api.getShares(
                    sortColumn = sortColumn,
                    sortOrder = sortOrder,
                    lang = lang,
                    page = page
                )
            }) {
                is Result.Error -> {
                    return MediatorResult.Error(result.error)
                }

                is Result.Data -> {
                    database.withTransaction {
                        if (loadType == LoadType.REFRESH) {
                            database.remoteKeysDao.getRemoteKeyByLabel(label)
                            database.sharesDao.deleteAll()
                        }
                    }

                    val data = result.data
                    val items = data.results.map { it.toEntity() }

                    val next = data.next
                    val nextKey = if (next == 0 || next == null) {
                        null
                    } else {
                        page.plus(1)
                    }

                    val prevKey = if (page == PageSettings.DEFAULT_START) null else page

                    database.remoteKeysDao.insertRemoteKey(
                        RemoteKeyEntity(
                            label = label,
                            prevKey = prevKey,
                            nextKey = nextKey
                        )
                    )

                    database.sharesDao.insertShares(items)
                    return MediatorResult.Success(endOfPaginationReached = items.isEmpty())
                }
            }

        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

}