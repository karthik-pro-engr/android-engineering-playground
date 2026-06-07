package com.karthik.pro.engr.github.api.playground.presentation.repo

import com.karthik.pro.engr.github.api.playground.presentation.repo.model.RepoDetailUi


sealed class RepoDetailUiState {
    data object Loading : RepoDetailUiState()
    data class Success(val data: RepoDetailUi) : RepoDetailUiState()
    data class Error(val message: String) : RepoDetailUiState()
}
