package com.karthik.pro.engr.github.api.playground.presentation.uistate

import com.karthik.pro.engr.github.api.domain.error.DomainError

sealed class ListUiState<out T> {
    data object Loading : ListUiState<Nothing>()
    data class Success<T>(val data: List<T>) : ListUiState<T>()
    data class Error(val error: DomainError) : ListUiState<Nothing>()
}
