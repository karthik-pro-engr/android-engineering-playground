package com.karthik.pro.engr.github.api.data.remote.mediator

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.karthik.pro.engr.github.api.data.local.dao.RemoteKeysDao
import com.karthik.pro.engr.github.api.data.local.dao.RepoDao
import com.karthik.pro.engr.github.api.data.local.database.GithubDatabase
import com.karthik.pro.engr.github.api.data.local.entity.RemoteKeysEntity
import com.karthik.pro.engr.github.api.data.local.entity.RepoEntity
import com.karthik.pro.engr.github.api.data.local.entity.RepoSearchEntity
import com.karthik.pro.engr.github.api.data.remote.api.GithubService
import com.karthik.pro.engr.github.api.data.remote.constants.NetworkConstants
import com.karthik.pro.engr.github.api.data.remote.constants.NetworkConstants.STARTING_PAGE_INDEX
import com.karthik.pro.engr.github.api.data.remote.error.ApiException
import com.karthik.pro.engr.github.api.data.remote.error.ErrorParser
import com.karthik.pro.engr.github.api.data.remote.mapper.RepoEntityMapper
import com.karthik.pro.engr.github.api.data.remote.pagination.GithubPaginationParser
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class GithubRemoteMediator(
    private val username: String,
    private val service: GithubService,
    private val repoDao: RepoDao,
    private val database: GithubDatabase,
    private val errorParser: ErrorParser,
    private val remoteKeysDao: RemoteKeysDao
) : RemoteMediator<Int, RepoEntity>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, RepoEntity>
    ): MediatorResult {
        return try {
            when (loadType) {
                LoadType.REFRESH -> {
                    val page = STARTING_PAGE_INDEX

                    val response = service.listUserRepos(
                        username,
                        state.config.pageSize,
                        page
                    )
                    if (response.isSuccessful) {
                        val repoDtos = response.body().orEmpty()
                        val repoEntities = RepoEntityMapper.fromDtoList(
                            repoDtos, username
                        )

                        val headerLink = response.headers()[NetworkConstants.HEADER_LINK]

                        val pageLinks =
                            GithubPaginationParser.parsePageNumbers(headerLink)



                        database.withTransaction {

                            val currentTime = System.currentTimeMillis()
                            val owner = repoDtos.firstOrNull()?.owner

                            repoDao.upsertSearchUser(
                                RepoSearchEntity(
                                    username = username,
                                    lastSync = currentTime,
                                    lastAccessed = currentTime,
                                    ownerId = owner?.id ?: 0L,
                                    avatarUrl = owner?.avatar_url,
                                    profileUrl = owner?.html_url
                                )
                            )

                            repoDao.deleteReposByUsername(username)

                            remoteKeysDao.deleteRemoteKeys(username)


                            repoDao.upsertRepos(repoEntities)

                            remoteKeysDao.upsertNextKey(
                                RemoteKeysEntity(
                                    username = username,
                                    nextPage = pageLinks.nextKey
                                )
                            )
                        }

                        MediatorResult.Success(
                            endOfPaginationReached = pageLinks.nextKey == null
                        )

                    } else {
                        val errorDto = errorParser.parse(response)
                        MediatorResult.Error(
                            ApiException(
                                message = errorDto.message,
                                code = response.code(),
                                headers = response.headers()
                            )
                        )

                    }

                }

                LoadType.PREPEND -> MediatorResult.Success(
                    endOfPaginationReached = true
                )

                LoadType.APPEND -> {
                    val nextPage = remoteKeysDao.getNextKey(username)


                    if (nextPage == null) {
                        MediatorResult.Success(
                            endOfPaginationReached = true
                        )
                    } else {

                        val response = service.listUserRepos(
                            username = username,
                            page = nextPage,
                            perPage = state.config.pageSize
                        )

                        if (response.isSuccessful) {
                            val repoDtos = response.body().orEmpty()

                            val repoEntities = RepoEntityMapper.fromDtoList(
                                repoDtos,
                                username
                            )

                            val pageLinks = GithubPaginationParser.parsePageNumbers(
                                response.headers()[NetworkConstants.HEADER_LINK]
                            )


                            database.withTransaction {

                                repoDao.upsertRepos(repoEntities)

                                remoteKeysDao.upsertNextKey(
                                    RemoteKeysEntity(
                                        username = username,
                                        nextPage = pageLinks.nextKey
                                    )
                                )
                            }

                            MediatorResult.Success(
                                endOfPaginationReached = pageLinks.nextKey == null
                            )
                        } else {

                            val errorDto = errorParser.parse(response)

                            MediatorResult.Error(
                                ApiException(
                                    message = errorDto.message,
                                    code = response.code(),
                                    headers = response.headers()
                                )
                            )

                        }

                    }

                }
            }
        } catch (e: IOException) {

            MediatorResult.Error(e)

        } catch (e: Exception) {

            MediatorResult.Error(e)
        }

    }

}