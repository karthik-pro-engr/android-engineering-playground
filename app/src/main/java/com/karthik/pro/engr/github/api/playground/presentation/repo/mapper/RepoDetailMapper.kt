package com.karthik.pro.engr.github.api.playground.presentation.repo.mapper

import com.karthik.pro.engr.github.api.domain.model.Release
import com.karthik.pro.engr.github.api.domain.model.Repo
import com.karthik.pro.engr.github.api.domain.time.DateFormatter
import com.karthik.pro.engr.github.api.playground.presentation.common.formatter.NumberFormatter
import com.karthik.pro.engr.github.api.playground.presentation.repo.components.releases.model.ReleaseUi
import com.karthik.pro.engr.github.api.playground.presentation.repo.model.RepoDetailUi


fun Repo.toRepoDetailUi() =
    RepoDetailUi(
        id = id,
        name = name,
        fullName = fullName,
        description = description.orEmpty(),
        htmlUrl = htmlUrl,
        language = language.orEmpty(),
        languagesUrl = languagesUrl,
        stars = NumberFormatter.readableCount(stars),
        forks = NumberFormatter.readableCount(forks),
        topics = topics,
        owner = owner,
    )


fun Release.toReleaseUi(dateFormatter: DateFormatter) = ReleaseUi(
    id = id,
    version = version,
    date = dateFormatter.format(isoDate = date),
    description = description,
    isLatest = true
)



