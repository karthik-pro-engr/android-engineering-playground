package com.karthik.pro.engr.github.api.domain.usecase

import com.karthik.pro.engr.github.api.domain.error.DomainError
import com.karthik.pro.engr.github.api.domain.repository.RepoDetailRepository
import com.karthik.pro.engr.github.api.domain.result.Result
import javax.inject.Inject


class RefreshLanguagesUseCase @Inject constructor(
    private val repository: RepoDetailRepository
) {

    suspend operator fun invoke(
        ownerName: String,
        repoName: String
    ): Result<Unit, DomainError> {
        return repository.refreshLanguages(
            ownerName,
            repoName
        )
    }
}