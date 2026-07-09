package com.karthik.pro.engr.github.api.data.remote.graphql.datasource

import com.apollographql.apollo.ApolloClient
import com.karthik.pro.engr.github.api.data.graphql.RepoDetailQuery
import javax.inject.Inject

class GithubGraphqlDataSourceImpl @Inject constructor(
    private val apolloClient: ApolloClient
) : GithubGraphqlDataSource {

    override suspend fun getRepoDetail(
        owner: String,
        repo: String
    ): RepoDetailQuery.Repository? {

        val response = apolloClient.query(
            RepoDetailQuery(
                owner = owner,
                repo = repo
            )
        ).execute()

        return response.data?.repository
    }
}