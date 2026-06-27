package com.karthik.pro.engr.github.api.core.testing

import com.karthik.pro.engr.github.api.domain.model.Repo

object FakeRepo {

    fun repo(
        id: Long = 1069192744,
        name: String = "admin-tools",
        description: String = "",
        language: String = "Shell",
        stars: Int = 5,
        forks: Int = 1,
        topics: List<String> = listOf(
            "android",
            "android-arcchitecture",
            "api-integration",
            "clean-architecture",
            "coroutines",
            "hilt",
            "jetpack-compose",
            "kotlin",
            "mock-webserver",
            "okhttp",
            "retrofit2",
            "room",
            "unit-testing"
        ),
        ownerName: String = "karthik-pro-engr"
    ): Repo = Repo(
        id = id,
        name = name,
        description = description,
        stars = stars,
        forks = forks,
        topics = topics,
        language = language,
        ownerName = ownerName
    )

}