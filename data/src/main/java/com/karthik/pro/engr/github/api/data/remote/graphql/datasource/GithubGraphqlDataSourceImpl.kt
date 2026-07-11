package com.karthik.pro.engr.github.api.data.remote.graphql.datasource

import com.apollographql.apollo.ApolloClient
import com.karthik.pro.engr.github.api.data.graphql.RepoDetailQuery
import com.karthik.pro.engr.github.api.data.remote.error.NetworkError
import com.karthik.pro.engr.github.api.data.remote.graphql.model.GraphqlRepoDetail
import javax.inject.Inject
import com.karthik.pro.engr.github.api.domain.result.Result
import com.apollographql.apollo.exception.ApolloHttpException
import com.apollographql.apollo.exception.ApolloNetworkException

class GithubGraphqlDataSourceImpl @Inject constructor(
    private val apolloClient: ApolloClient
) : GithubGraphqlDataSource {

    override suspend fun getRepoDetail(
        owner: String,
        repo: String
    ): Result<GraphqlRepoDetail, NetworkError> {

        return try {

            val response = apolloClient
                .query(
                    RepoDetailQuery(
                        owner = owner,
                        repo = repo
                    )
                )
                .execute()

            response.exception?.let { exception ->

                return when (exception) {

                    is ApolloNetworkException ->
                        Result.Failure(
                            NetworkError.NoInternet
                        )

                    is ApolloHttpException ->
                        Result.Failure(
                            NetworkError.Http(
                                exception.statusCode
                            )
                        )

                    else ->
                        Result.Failure(
                            NetworkError.Unknown
                        )
                }
            }

            if (response.errors?.isNotEmpty() == true) {
                return Result.Failure(
                    NetworkError.Unknown
                )
            }

            val repository = response.data?.repository

            return if (repository != null) {
                Result.Success(repository)
            } else {
                Result.Failure(
                    NetworkError.Unknown
                )
            }

        } catch (e: Exception) {

            Result.Failure(
                NetworkError.Unknown
            )
        }
    }
}