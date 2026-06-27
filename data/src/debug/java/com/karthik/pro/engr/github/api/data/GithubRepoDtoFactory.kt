package com.karthik.pro.engr.github.api.data

import com.karthik.pro.engr.github.api.data.remote.dto.response.GitHubOwnerDto
import com.karthik.pro.engr.github.api.data.remote.dto.response.GitHubRepoDto

object GithubRepoDtoFactory {

    fun create(
        id: Long = 1L,
        name: String = "compose-playground",
        ownerName: String = "karthik-pro-engr"
    ): GitHubRepoDto {

        return GitHubRepoDto(
            id = id,
            name = name,
            description = "Sample Description",
            language = "Kotlin",
            stargazers_count = 100,
            forks_count = 25,
            topics = listOf(
                "android",
                "compose"
            ),
            owner = GitHubOwnerDto(
                login = ownerName,
                id = 100L,
                avatar_url = "avatar-url",
                html_url = "profile-url"
            )
        )
    }
}