package com.karthik.pro.engr.github.api.core.testing

import com.karthik.pro.engr.github.api.core.testing.repo_detail.FakeLanguage
import com.karthik.pro.engr.github.api.core.testing.repo_detail.FakeReleases

object RepoFactory {
    fun defaultRepo() = FakeRepo.repo()

    fun withId(id: Long) = FakeRepo.repo(id = id)

    fun withTopics(topics: List<String>) = FakeRepo.repo(topics = topics)

    fun defaultLanguages() = FakeLanguage.languages()

    fun defaultReleases() = FakeReleases.releases()

    fun defaultReleaseItem() = FakeReleases.releaseItem()

}