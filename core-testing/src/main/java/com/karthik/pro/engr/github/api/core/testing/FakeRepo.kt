package com.karthik.pro.engr.github.api.core.testing

import com.karthik.pro.engr.github.api.domain.model.Owner
import com.karthik.pro.engr.github.api.domain.model.Repo

object FakeRepo {

    fun repo(
        id: Long = 1069192744,
        name: String = "admin-tools",
        fullName: String = "karthik-pro-engr/admin-tools",
        description: String = "",
        htmlUrl: String = "https://github.com/karthik-pro-engr",
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
        languagesUrl: String = "https://api.github.com/repos/karthik-pro-engr/admin-tools/languages",
        owner: Owner = owner()
    ): Repo = Repo(
        id = id,
        name = name,
        fullName = fullName,
        description = description,
        htmlUrl = htmlUrl,
        language = language,
        stars = stars,
        forks = forks,
        topics = topics,
        languagesUrl = languagesUrl,
        owner = owner
    )


    private fun owner(
        name: String = "karthik-pro-engr",
        id: Long = 101930095,
        profilePictureUrl: String = "https://avatars.githubusercontent.com/u/101930095?v=4",
        htmlUrl: String = "https://github.com/karthik-pro-engr"
    ): Owner = Owner(
        name = name,
        id = id,
        profilePictureUrl = profilePictureUrl,
        htmlUrl = htmlUrl
    )

}