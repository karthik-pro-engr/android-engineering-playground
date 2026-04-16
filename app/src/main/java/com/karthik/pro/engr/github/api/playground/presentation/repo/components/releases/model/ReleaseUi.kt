package com.karthik.pro.engr.github.api.playground.presentation.repo.components.releases.model

data class ReleaseUi(
    val id: Long,
    val version: String,
    val date: String,
    val isLatest: Boolean
)