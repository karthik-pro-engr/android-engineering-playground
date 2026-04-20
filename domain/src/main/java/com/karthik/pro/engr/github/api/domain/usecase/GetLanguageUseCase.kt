package com.karthik.pro.engr.github.api.domain.usecase

import com.karthik.pro.engr.github.api.domain.model.Language
import com.karthik.pro.engr.github.api.domain.repository.GithubRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLanguageUseCase @Inject constructor(private val repository: GithubRepository) {

    operator fun invoke(
        ownerName: String,
        repoName: String
    ): Flow<List<Language>> {
        return repository.getLanguage(ownerName, repoName)
    }
}