package com.karthik.pro.engr.github.api.data.remote.mapper

import com.karthik.pro.engr.github.api.data.remote.dto.response.GitHubRepoDto
import com.karthik.pro.engr.github.api.domain.model.Repo

object RepoMapper {
    fun fromDto(dto: GitHubRepoDto) = Repo(
        id = dto.id,
        name = dto.name,
        description = dto.description,
        stars = dto.stargazers_count,
        language = dto.language,
        forks = dto.forks_count,
        topics = dto.topics,
        ownerName =dto.owner.login
    )

    fun fromDtoList(dtos: List<GitHubRepoDto>): List<Repo> = dtos.map { fromDto(it) }
}