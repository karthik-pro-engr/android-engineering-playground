package com.karthik.pro.engr.github.api.domain.repository

import androidx.paging.PagingData
import com.karthik.pro.engr.github.api.domain.error.DomainError
import com.karthik.pro.engr.github.api.domain.model.Language
import com.karthik.pro.engr.github.api.domain.model.Release
import com.karthik.pro.engr.github.api.domain.model.Repo
import com.karthik.pro.engr.github.api.domain.result.Result
import kotlinx.coroutines.flow.Flow


interface GithubRepository {
    fun getUserRepos(
        username: String
    ): Flow<PagingData<Repo>>

    suspend fun getRepoDetail(
        ownerName: String,
        repoName: String
    ): Result<Repo, DomainError>

    suspend fun getLanguage(
        ownerName: String,
        repoName: String
    ): Result<Map<String, Long>, DomainError>

    suspend fun getReleases(
        ownerName: String,
        repoName: String
    ): Result<List<Release>, DomainError>

    suspend fun cleanupInactiveData()

}