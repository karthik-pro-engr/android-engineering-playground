package com.karthik.pro.engr.github.api.playground.presentation.repos

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.karthik.pro.engr.github.api.data.remote.graphql.datasource.GithubGraphqlDataSourceImpl
import com.karthik.pro.engr.github.api.domain.model.Repo
import com.karthik.pro.engr.github.api.domain.usecase.CleanupInactiveDataUseCase
import com.karthik.pro.engr.github.api.domain.usecase.GetUserReposUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GithubReposListViewModel @Inject constructor(
    private val useCase: GetUserReposUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {


    private var _committedQuery = MutableStateFlow<String?>(savedStateHandle[KEY_USER_NAME_QUERY])
    val currentQuery: StateFlow<String?> = _committedQuery.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val reposSharedFlow: Flow<PagingData<Repo>> = _committedQuery
        .filterNotNull()
        .distinctUntilChanged()
        .flatMapLatest { userName ->
            useCase(userName)
                .cachedIn(viewModelScope)
        }
        .shareIn(viewModelScope,
            SharingStarted.Lazily,
            PAGING_REPLAY)

    init {

        viewModelScope.launch {
            _committedQuery.collect { query ->
                savedStateHandle[KEY_USER_NAME_QUERY] = query
            }
        }
    }


    fun submitQuery(username: String) {
        val trimmed = username.trim()
        _committedQuery.value = trimmed.ifEmpty { null }
    }


    companion object {
        private const val KEY_USER_NAME_QUERY = "last_user_name"
        private const val PAGING_REPLAY = 1
    }
}