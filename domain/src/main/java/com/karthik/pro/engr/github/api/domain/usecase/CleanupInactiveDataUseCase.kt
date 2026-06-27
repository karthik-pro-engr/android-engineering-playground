package com.karthik.pro.engr.github.api.domain.usecase

import com.karthik.pro.engr.github.api.domain.repository.GithubRepository
import javax.inject.Inject

class CleanupInactiveDataUseCase @Inject constructor(
    private val repository: GithubRepository
) {
    suspend operator fun invoke() {
        repository.cleanupInactiveData()
    }
}