package com.karthik.pro.engr.github.api.data.remote.graphql.datasource

import com.karthik.pro.engr.github.api.data.remote.error.NetworkError
import com.karthik.pro.engr.github.api.data.remote.graphql.model.GraphqlRepoDetail
import com.karthik.pro.engr.github.api.domain.result.Result

interface GithubGraphqlDataSource {

    suspend fun getRepoDetail(
        owner: String,
        repo: String
    ): Result<GraphqlRepoDetail, NetworkError>
}