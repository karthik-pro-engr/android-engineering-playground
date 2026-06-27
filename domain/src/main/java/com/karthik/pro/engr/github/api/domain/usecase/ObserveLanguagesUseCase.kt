package com.karthik.pro.engr.github.api.domain.usecase

import com.karthik.pro.engr.github.api.domain.model.Language
import com.karthik.pro.engr.github.api.domain.repository.RepoDetailRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveLanguagesUseCase @Inject constructor(
    private val repository: RepoDetailRepository
) {

    operator fun invoke(
        ownerName: String,
        repoName: String
    ): Flow<List<Language>> {
        return repository.observeLanguages(
            ownerName,
            repoName
        )
    }
}