package com.karthik.pro.engr.github.api.playground.presentation.repo

import androidx.lifecycle.SavedStateHandle

object SavedStateHandleFactory {

    fun repoDetail(
        owner: String = "karthik-pro-engr",
        repo: String = "github-api-playground"
    ) = SavedStateHandle(
        mapOf(
            "owner" to owner,
            "repo" to repo
        )
    )
}