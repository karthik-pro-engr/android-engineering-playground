package com.karthik.pro.engr.github.api.data.remote.graphql.datasource

import com.karthik.pro.engr.github.api.data.graphql.RepoDetailQuery

interface GithubGraphqlDataSource {

    suspend fun getRepoDetail(
        owner: String,
        repo: String
    ): RepoDetailQuery.Repository?
}