package com.karthik.pro.engr.github.api.data.remote.mapper

import com.karthik.pro.engr.github.api.data.local.entity.RepoEntity
import com.karthik.pro.engr.github.api.data.remote.dto.response.GitHubRepoDto

object RepoEntityMapper {

    fun fromDto(
        dto: GitHubRepoDto,
        username: String
    ): RepoEntity {
        return RepoEntity(
            id = dto.id,
            name = dto.name,
            description = dto.description,
            language = dto.language.orEmpty(),
            stars = dto.stargazers_count,
            forks = dto.forks_count,
            topics = dto.topics,
            username = username
        )
    }

    fun fromDtoList(
        dtos: List<GitHubRepoDto>,
        username: String
    ): List<RepoEntity> {
        return dtos.map {
            fromDto(it, username)
        }
    }


}