package com.karthik.pro.engr.github.api.playground.presentation.repo.model

import com.karthik.pro.engr.github.api.domain.model.Language
import com.karthik.pro.engr.github.api.playground.presentation.common.UiText
import com.karthik.pro.engr.github.api.playground.presentation.repo.components.header.model.HeaderUi
import com.karthik.pro.engr.github.api.playground.presentation.repo.components.releases.model.ReleaseUi
import com.karthik.pro.engr.github.api.playground.presentation.repo.components.stats.StatsUi

sealed class RepoDetailItemUi {
    data object HeaderLoading : RepoDetailItemUi()
    data class HeaderSuccess(val data: HeaderUi) : RepoDetailItemUi()
    data class HeaderError(val message: String) : RepoDetailItemUi()
    data class Stats(val statsUi: StatsUi) : RepoDetailItemUi()

    data class SectionTitle(val title: UiText) : RepoDetailItemUi()
    data object LanguageLoading : RepoDetailItemUi()
    data class LanguageError(val message: String) : RepoDetailItemUi()
    data class LanguageSuccess(val data: List<Language>) : RepoDetailItemUi()
    data class Topics(val topics: List<String>) : RepoDetailItemUi()
    data class ReleaseError(val message: String) : RepoDetailItemUi()
    data object ReleaseLoading : RepoDetailItemUi()
    data class ReleaseSuccess(val data: ReleaseUi) : RepoDetailItemUi()
    data object NoReleases : RepoDetailItemUi()
}

fun RepoDetailItemUi.key(): String = when (this) {

    is RepoDetailItemUi.SectionTitle -> "section_${title}"

    // Header
    RepoDetailItemUi.HeaderLoading -> "header_loading"
    is RepoDetailItemUi.HeaderSuccess -> "header_${data.title}"
    is RepoDetailItemUi.HeaderError -> "header_error"

    // Stats
    is RepoDetailItemUi.Stats -> "stats"

    // Topics
    is RepoDetailItemUi.Topics -> "topics"

    // Language
    RepoDetailItemUi.LanguageLoading -> "language_loading"
    is RepoDetailItemUi.LanguageError -> "language_error"
    is RepoDetailItemUi.LanguageSuccess -> "languages_list"

    // Releases
    RepoDetailItemUi.ReleaseLoading -> "release_loading"
    is RepoDetailItemUi.ReleaseError -> "release_error"
    is RepoDetailItemUi.ReleaseSuccess -> "release_${data.id}"
    is RepoDetailItemUi.NoReleases -> "no_release"
}
