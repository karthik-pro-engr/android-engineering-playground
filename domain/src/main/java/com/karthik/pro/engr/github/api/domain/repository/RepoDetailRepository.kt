package com.karthik.pro.engr.github.api.domain.repository

import com.karthik.pro.engr.github.api.domain.error.DomainError
import com.karthik.pro.engr.github.api.domain.model.Language
import com.karthik.pro.engr.github.api.domain.model.Release
import com.karthik.pro.engr.github.api.domain.model.Repo
import kotlinx.coroutines.flow.Flow
import com.karthik.pro.engr.github.api.domain.result.Result

interface RepoDetailRepository {

    fun observeRepoDetail(
        ownerName: String,
        repoName: String
    ): Flow<Repo?>

    suspend fun refreshRepoDetail(
        ownerName: String,
        repoName: String
    ): Result<Unit, DomainError>

    fun observeLanguages(
        ownerName: String,
        repoName: String
    ): Flow<List<Language>>

    suspend fun refreshLanguages(
        ownerName: String,
        repoName: String
    ): Result<Unit, DomainError>

    fun observeReleases(
        ownerName: String,
        repoName: String
    ): Flow<List<Release>>

    suspend fun refreshReleases(
        ownerName: String,
        repoName: String
    ): Result<Unit, DomainError>
}