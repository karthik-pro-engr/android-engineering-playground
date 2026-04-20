package com.karthik.pro.engr.github.api.data.repository

import androidx.paging.PagingData
import com.karthik.pro.engr.github.api.core.testing.RepoFactory
import com.karthik.pro.engr.github.api.data.remote.mapper.toLanguageList
import com.karthik.pro.engr.github.api.domain.model.Language
import com.karthik.pro.engr.github.api.domain.model.Release
import com.karthik.pro.engr.github.api.domain.model.Repo
import com.karthik.pro.engr.github.api.domain.repository.GithubRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class FakeRepository @Inject constructor() : GithubRepository {
    override fun getUserRepos(username: String): Flow<PagingData<Repo>> {
        TODO("Not yet implemented")
    }

    override fun getRepoDetail(
        ownerName: String,
        repoName: String
    ): Flow<Repo> = flowOf(RepoFactory.defaultRepo())


    override fun getLanguage(
        ownerName: String,
        repoName: String
    ): Flow<List<Language>> = flowOf(RepoFactory.defaultLanguages().toLanguageList())

    override fun getReleases(
        ownerName: String,
        repoName: String
    ): Flow<List<Release>> = flowOf(RepoFactory.defaultReleases())
}