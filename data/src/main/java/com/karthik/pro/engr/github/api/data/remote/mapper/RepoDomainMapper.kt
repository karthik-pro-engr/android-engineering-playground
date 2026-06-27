package com.karthik.pro.engr.github.api.data.remote.mapper

import com.karthik.pro.engr.github.api.data.local.entity.RepoEntity
import com.karthik.pro.engr.github.api.domain.model.Repo

object RepoDomainMapper {

    fun fromEntity(entity: RepoEntity): Repo {
        return Repo(
            id = entity.id,
            name = entity.name,
            description = entity.description,
            language = entity.language,
            stars = entity.stars,
            forks = entity.forks,
            topics = entity.topics,
            ownerName = entity.username
        )
    }

    fun fromEntityList(
        entities: List<RepoEntity>
    ): List<Repo> {
        return entities.map(::fromEntity)
    }
}