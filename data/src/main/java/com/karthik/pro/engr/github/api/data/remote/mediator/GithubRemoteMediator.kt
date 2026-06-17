package com.karthik.pro.engr.github.api.data.remote.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.karthik.pro.engr.github.api.data.local.dao.RepoDao
import com.karthik.pro.engr.github.api.data.local.entity.RepoEntity
import com.karthik.pro.engr.github.api.data.remote.api.GithubService

@OptIn(ExperimentalPagingApi::class)
class GithubRemoteMediator(
    private val username: String,
    private val service: GithubService,
    private val repoDao: RepoDao
) : RemoteMediator<Int, RepoEntity>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, RepoEntity>
    ): MediatorResult {
        return MediatorResult.Success(
            endOfPaginationReached = true
        )
    }

}