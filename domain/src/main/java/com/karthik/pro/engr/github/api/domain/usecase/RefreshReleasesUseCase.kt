package com.karthik.pro.engr.github.api.domain.usecase

import com.karthik.pro.engr.github.api.domain.error.DomainError
import com.karthik.pro.engr.github.api.domain.repository.RepoDetailRepository
import javax.inject.Inject
import com.karthik.pro.engr.github.api.domain.result.Result

class RefreshReleasesUseCase @Inject constructor(
    private val repository: RepoDetailRepository
) {

    suspend operator fun invoke(
        ownerName: String,
        repoName: String
    ): Result<Unit, DomainError> {
        return repository.refreshReleases(
            ownerName,
            repoName
        )
    }
}