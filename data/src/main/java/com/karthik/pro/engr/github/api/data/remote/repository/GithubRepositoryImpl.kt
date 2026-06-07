package com.karthik.pro.engr.github.api.data.remote.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.karthik.pro.engr.github.api.core.di.IoDispatcher
import com.karthik.pro.engr.github.api.data.remote.api.GithubService
import com.karthik.pro.engr.github.api.data.remote.error.ErrorParser
import com.karthik.pro.engr.github.api.data.remote.mapper.ReleaseMapper
import com.karthik.pro.engr.github.api.data.remote.mapper.RepoMapper.fromDto
import com.karthik.pro.engr.github.api.data.remote.mapper.toDomainError
import com.karthik.pro.engr.github.api.data.remote.pagination.GithubPagingSource
import com.karthik.pro.engr.github.api.data.remote.util.safeApiCall
import com.karthik.pro.engr.github.api.domain.constants.PaginationConstants.DEFAULT_PAGE_SIZE
import com.karthik.pro.engr.github.api.domain.error.DomainError
import com.karthik.pro.engr.github.api.domain.model.Release
import com.karthik.pro.engr.github.api.domain.model.Repo
import com.karthik.pro.engr.github.api.domain.repository.GithubRepository
import com.karthik.pro.engr.github.api.domain.result.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GithubRepositoryImpl @Inject constructor(
    private val service: GithubService,
    private val errorParser: ErrorParser,
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : GithubRepository {
    override fun getUserRepos(
        username: String,
    ): Flow<PagingData<Repo>> {
        val pageSize = DEFAULT_PAGE_SIZE
        return Pager(
            config = PagingConfig(pageSize = pageSize, enablePlaceholders = false),
            pagingSourceFactory = {
                GithubPagingSource(
                    service = service,
                    errorParser = errorParser,
                    username = username,
                    perPage = pageSize
                )
            }
        ).flow
    }

    override suspend fun getRepoDetail(
        ownerName: String,
        repoName: String
    ): Result<Repo, DomainError> {
        return withContext(ioDispatcher) {
            when (val result = safeApiCall {
                service.repoDetail(
                    ownerName,
                    repoName
                )
            }) {
                is Result.Success -> {
                    Result.Success(fromDto(result.data))
                }

                is Result.Failure -> {
                    Result.Failure(result.error.toDomainError())

                }
            }
        }
    }

    override suspend fun getLanguage(
        ownerName: String,
        repoName: String
    ): Result<Map<String, Long>, DomainError> {
        return withContext(ioDispatcher) {
            when (val result = safeApiCall { service.getRepoLanguages(ownerName, repoName) }) {
                is Result.Success -> {
                    Result.Success(result.data)
                }

                is Result.Failure -> {
                    Result.Failure(result.error.toDomainError())
                }
            }

        }
    }

    override suspend fun getReleases(
        ownerName: String,
        repoName: String
    ): Result<List<Release>, DomainError> {
        return withContext(ioDispatcher) {
            when (val result = safeApiCall { service.getReleases(ownerName, repoName) }) {
                is Result.Success -> {
                    Result.Success(ReleaseMapper.fromReleaseDtoList(result.data))
                }

                is Result.Failure -> {
                    Result.Failure(result.error.toDomainError())
                }
            }
        }
    }
}