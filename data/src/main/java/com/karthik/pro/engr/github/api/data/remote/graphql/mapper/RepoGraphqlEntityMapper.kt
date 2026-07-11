package com.karthik.pro.engr.github.api.data.remote.graphql.mapper

import com.karthik.pro.engr.github.api.data.local.entity.RepoEntity
import com.karthik.pro.engr.github.api.data.remote.graphql.model.GraphqlRepoDetail

object RepoGraphqlEntityMapper {

    fun fromGraphql(
        repository: GraphqlRepoDetail,
        username: String
    ): RepoEntity {

        val databaseId = requireNotNull(
            repository.databaseId
        ) {
            "Repository '${repository.name}' has null databaseId"
        }

        return RepoEntity(
            id = databaseId.toLong(),
            name = repository.name,
            description = repository.description,
            language = repository.primaryLanguage?.name.orEmpty(),
            stars = repository.stargazerCount,
            forks = repository.forkCount,
            topics = repository.repositoryTopics.topicNodes
                ?.mapNotNull {
                    it?.topic?.name
                }
                ?: emptyList(),
            username = username
        )
    }
}