package com.karthik.pro.engr.github.api.data.mapper

import com.google.common.truth.Truth.assertThat
import com.karthik.pro.engr.github.api.data.remote.dto.response.GitHubOwnerDto
import com.karthik.pro.engr.github.api.data.remote.dto.response.GitHubRepoDto
import com.karthik.pro.engr.github.api.data.remote.mapper.RepoMapper
import org.junit.Test

class RepoMapperTest {

    @Test
    fun `maps GithubRepoDto to domain Repo correctly`() {
        val dto = GitHubRepoDto(
            id = 123,
            name = "compose-playground",
            description = "A demo",
            stargazers_count = 42,
            language = "Kotlin",
            forks_count = 0,
            owner = GitHubOwnerDto(
                login = "karthik-pro-engr",
                id = 1,
                avatar_url = "https://avatars.githubusercontent.com/u/101930095?v=4",
                html_url = "https://github.com/karthik-pro-engr"
            ),
            topics = emptyList()
        )
        val domainRepo = RepoMapper.fromDto(dto)
        assertThat(domainRepo.id).isEqualTo(123)
        assertThat(domainRepo.name).isEqualTo("compose-playground")
        assertThat(domainRepo.ownerName).isEqualTo("karthik-pro-engr")
        assertThat(domainRepo.stars).isEqualTo(42)
        assertThat(domainRepo.language).isEqualTo("Kotlin")
    }
}