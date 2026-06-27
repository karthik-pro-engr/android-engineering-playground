package com.karthik.pro.engr.github.api.playground.app.preview

import com.karthik.pro.engr.github.api.core.testing.RepoFactory
import com.karthik.pro.engr.github.api.data.remote.mapper.toLanguageList
import com.karthik.pro.engr.github.api.playground.R
import com.karthik.pro.engr.github.api.playground.presentation.common.UiText
import com.karthik.pro.engr.github.api.playground.presentation.common.formatter.RelativeTimeFormatter
import com.karthik.pro.engr.github.api.playground.presentation.repo.components.header.model.HeaderUi
import com.karthik.pro.engr.github.api.playground.presentation.repo.components.stats.StatsUi
import com.karthik.pro.engr.github.api.playground.presentation.repo.mapper.toReleaseUi
import com.karthik.pro.engr.github.api.playground.presentation.repo.mapper.toRepoDetailUi
import com.karthik.pro.engr.github.api.playground.presentation.repo.model.RepoDetailItemUi

fun fakeItems(): List<RepoDetailItemUi> {

    val repo = RepoFactory.defaultRepo().toRepoDetailUi()
    val languages = RepoFactory.defaultLanguages().toLanguageList()
    val releases = RepoFactory.defaultReleases().map {
        it.toReleaseUi(RelativeTimeFormatter())
    }

    return buildList {

        add(
            RepoDetailItemUi.HeaderSuccess(
                HeaderUi(repo.ownerName, repo.name, repo.description.orEmpty())
            )
        )

        add(RepoDetailItemUi.Stats(StatsUi(repo.stars, repo.forks)))

        add(RepoDetailItemUi.SectionTitle(UiText.StringRes(R.string.topics)))

        add(RepoDetailItemUi.Topics(repo.topics))

        add(RepoDetailItemUi.SectionTitle(UiText.StringRes(R.string.languages)))


        add(RepoDetailItemUi.LanguageSuccess(languages))

        add(RepoDetailItemUi.SectionTitle(UiText.StringRes(R.string.releases)))

        addAll(
            releases.map {
                RepoDetailItemUi.ReleaseSuccess(it)
            }
        )
    }
}