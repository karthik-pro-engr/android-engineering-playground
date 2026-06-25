package com.karthik.pro.engr.github.api.domain.usecase

import com.karthik.pro.engr.github.api.domain.model.Repo
import com.karthik.pro.engr.github.api.domain.repository.RepoDetailRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveRepoDetailUseCase @Inject constructor(
    private val repository: RepoDetailRepository
) {

    operator fun invoke(
        ownerName: String,
        repoName: String
    ): Flow<Repo?> {
        return repository.observeRepoDetail(
            ownerName,
            repoName
        )
    }
}