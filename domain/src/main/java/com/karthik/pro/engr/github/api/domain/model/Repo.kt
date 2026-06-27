package com.karthik.pro.engr.github.api.domain.model

data class Repo(
    val id: Long,
    val name: String,
    val description: String?,
    val language: String?,
    val stars: Int,
    val forks: Int,
    val topics: List<String>,
    val ownerName: String
)
