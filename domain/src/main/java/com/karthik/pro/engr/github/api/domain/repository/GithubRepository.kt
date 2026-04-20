package com.karthik.pro.engr.github.api.domain.repository

import androidx.paging.PagingData
import com.karthik.pro.engr.github.api.domain.model.Language
import com.karthik.pro.engr.github.api.domain.model.Release
import com.karthik.pro.engr.github.api.domain.model.Repo
import kotlinx.coroutines.flow.Flow


interface GithubRepository {
    fun getUserRepos(
        username: String
    ): Flow<PagingData<Repo>>

    fun getRepoDetail(
        ownerName: String,
        repoName: String
    ): Flow<Repo>

    fun getLanguage(
        ownerName: String,
        repoName: String
    ): Flow<List<Language>>

    fun getReleases(
        ownerName: String,
        repoName: String
    ): Flow<List<Release>>
}