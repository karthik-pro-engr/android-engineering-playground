package com.karthik.pro.engr.github.api.data.remote.graphql.mapper

import com.karthik.pro.engr.github.api.data.graphql.RepoDetailQuery
import com.karthik.pro.engr.github.api.data.local.entity.RepoReleaseEntity
import com.karthik.pro.engr.github.api.data.remote.graphql.model.GraphqlRelease
import com.karthik.pro.engr.github.api.data.remote.graphql.model.GraphqlRepoDetail

import kotlin.collections.mapNotNull

object RepoGraphqlReleaseMapper {

    fun fromGraphql(
        repoId: Long,
        releases: List<GraphqlRelease?>
    ): List<RepoReleaseEntity> {

        return releases.mapNotNull { release ->

            release ?: return@mapNotNull null

            val databaseId = release.databaseId
                ?: return@mapNotNull null

            RepoReleaseEntity(
                id = databaseId.toLong(),
                repoId = repoId,
                title = release.name.orEmpty(),
                version = release.tagName,
                description = release.description.orEmpty(),
                date = release.publishedAt?.toString().orEmpty()
            )
        }
    }
}