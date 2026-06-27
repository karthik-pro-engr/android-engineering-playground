package com.karthik.pro.engr.github.api.domain.usecase

import com.karthik.pro.engr.github.api.domain.model.Release
import com.karthik.pro.engr.github.api.domain.repository.RepoDetailRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveReleasesUseCase @Inject constructor(
    private val repository: RepoDetailRepository
) {

    operator fun invoke(
        ownerName: String,
        repoName: String
    ): Flow<List<Release>> {
        return repository.observeReleases(
            ownerName,
            repoName
        )
    }
}