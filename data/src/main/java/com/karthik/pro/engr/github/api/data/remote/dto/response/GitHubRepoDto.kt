package com.karthik.pro.engr.github.api.data.remote.dto.response

data class GitHubRepoDto(
    val id: Long,
    val name: String,
    val description: String?,
    val language: String?,
    val stargazers_count: Int,
    val forks_count: Int,
    val topics:List<String>,
    val owner: GitHubOwnerDto
)
