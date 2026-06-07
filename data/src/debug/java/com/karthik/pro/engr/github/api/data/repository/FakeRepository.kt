package com.karthik.pro.engr.github.api.data.repository

import androidx.paging.PagingData
import com.karthik.pro.engr.github.api.core.testing.RepoFactory
import com.karthik.pro.engr.github.api.data.remote.mapper.toLanguageList
import com.karthik.pro.engr.github.api.domain.error.DomainError
import com.karthik.pro.engr.github.api.domain.model.Language
import com.karthik.pro.engr.github.api.domain.model.Release
import com.karthik.pro.engr.github.api.domain.model.Repo
import com.karthik.pro.engr.github.api.domain.repository.GithubRepository
import com.karthik.pro.engr.github.api.domain.result.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class FakeRepository @Inject constructor() : GithubRepository {
    override fun getUserRepos(username: String): Flow<PagingData<Repo>> = flowOf(
        PagingData.from(
            listOf(
                RepoFactory.defaultRepo()
            )
        )
    )

    override suspend fun getRepoDetail(
        ownerName: String,
        repoName: String
    ): Result<Repo, DomainError> = Result.Success(RepoFactory.defaultRepo())


    override suspend fun getLanguage(
        ownerName: String,
        repoName: String
    ): Result<Map<String, Long>, DomainError> = Result.Success(RepoFactory.defaultLanguages())

    override suspend fun getReleases(
        ownerName: String,
        repoName: String
    ): Result<List<Release>, DomainError> = Result.Success(RepoFactory.defaultReleases())
}