package com.karthik.pro.engr.github.api.domain.usecase


import com.karthik.pro.engr.github.api.domain.error.DomainError
import com.karthik.pro.engr.github.api.domain.model.Repo
import com.karthik.pro.engr.github.api.domain.repository.GithubRepository
import com.karthik.pro.engr.github.api.domain.result.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRepoDetailUseCase @Inject constructor(private val repository: GithubRepository) {
    suspend operator fun invoke(
        ownerName: String,
        repoName: String
    ) = repository.getRepoDetail(ownerName, repoName)
}