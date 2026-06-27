package com.karthik.pro.engr.github.api.playground.presentation.repo.model

import com.karthik.pro.engr.github.api.domain.model.Owner

data class RepoDetailUi(
    val id: Long,
    val name: String,
    val description: String?,
    val language: String?,
    val stars: String,
    val forks: String,
    val topics: List<String>,
    val ownerName: String
)
