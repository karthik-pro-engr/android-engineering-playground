package com.karthik.pro.engr.github.api.domain.usecase

import com.karthik.pro.engr.github.api.domain.model.Language
import com.karthik.pro.engr.github.api.domain.repository.GithubRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLanguageUseCase @Inject constructor(private val repository: GithubRepository) {

    suspend operator fun invoke(
        ownerName: String,
        repoName: String
    ) = repository.getLanguage(ownerName, repoName)
}