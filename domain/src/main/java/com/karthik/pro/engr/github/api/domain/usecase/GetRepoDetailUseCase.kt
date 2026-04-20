package com.karthik.pro.engr.github.api.domain.usecase


import com.karthik.pro.engr.github.api.domain.model.Repo
import com.karthik.pro.engr.github.api.domain.repository.GithubRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRepoDetailUseCase @Inject constructor(private val repository: GithubRepository) {
    operator fun invoke(
        ownerName: String,
        repoName: String
    ): Flow<Repo> {
        return repository.getRepoDetail(ownerName, repoName)
    }
}