package com.karthik.pro.engr.github.api.data.remote.mapper

import com.karthik.pro.engr.github.api.data.local.entity.RepoReleaseEntity
import com.karthik.pro.engr.github.api.domain.model.Release

object RepoReleaseMapper {

    fun fromDomain(
        repoId: Long,
        releases: List<Release>
    ): List<RepoReleaseEntity> {

        return releases.map {

            RepoReleaseEntity(
                id = it.id,
                repoId = repoId,
                title = it.title,
                version = it.version,
                description = it.description,
                date = it.date
            )
        }
    }

    fun toDomain(
        entities: List<RepoReleaseEntity>
    ): List<Release> {

        return entities.map {

            Release(
                id = it.id,
                title = it.title,
                version = it.version,
                description = it.description,
                date = it.date
            )
        }
    }
}