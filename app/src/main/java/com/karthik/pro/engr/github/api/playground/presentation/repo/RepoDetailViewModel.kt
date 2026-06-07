package com.karthik.pro.engr.github.api.playground.presentation.repo

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.karthik.pro.engr.github.api.data.remote.mapper.toLanguageList
import com.karthik.pro.engr.github.api.domain.error.DomainError
import com.karthik.pro.engr.github.api.domain.model.Language
import com.karthik.pro.engr.github.api.domain.result.Result
import com.karthik.pro.engr.github.api.domain.usecase.GetLanguageUseCase
import com.karthik.pro.engr.github.api.domain.usecase.GetReleasesUseCase
import com.karthik.pro.engr.github.api.domain.usecase.GetRepoDetailUseCase
import com.karthik.pro.engr.github.api.playground.R
import com.karthik.pro.engr.github.api.playground.presentation.common.UiText
import com.karthik.pro.engr.github.api.playground.presentation.common.formatter.RelativeTimeFormatter
import com.karthik.pro.engr.github.api.playground.presentation.error.ApiErrorMapper
import com.karthik.pro.engr.github.api.playground.presentation.navigation.AppDestinations.REPO_NAME_ARG
import com.karthik.pro.engr.github.api.playground.presentation.navigation.AppDestinations.REPO_OWNER_ARG
import com.karthik.pro.engr.github.api.playground.presentation.repo.components.header.model.HeaderUi
import com.karthik.pro.engr.github.api.playground.presentation.repo.components.releases.model.ReleaseUi
import com.karthik.pro.engr.github.api.playground.presentation.repo.components.stats.StatsUi
import com.karthik.pro.engr.github.api.playground.presentation.repo.mapper.toReleaseUi
import com.karthik.pro.engr.github.api.playground.presentation.repo.mapper.toRepoDetailUi
import com.karthik.pro.engr.github.api.playground.presentation.repo.model.RepoDetailItemUi
import com.karthik.pro.engr.github.api.playground.presentation.uistate.ListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RepoDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getRepoDetailUseCase: GetRepoDetailUseCase,
    private val getLanguageUseCase: GetLanguageUseCase,
    private val getReleasesUseCase: GetReleasesUseCase
) : ViewModel() {

    private val owner: String = savedStateHandle[REPO_OWNER_ARG] ?: ""
    val repoName: String = savedStateHandle[REPO_NAME_ARG] ?: ""


    private var _repoUiState = MutableStateFlow<RepoDetailUiState>(RepoDetailUiState.Loading)
    val repoUiState = _repoUiState.asStateFlow()

    private var _languageUiState = MutableStateFlow<ListUiState<Language>>(ListUiState.Loading)
    val languageUiState = _languageUiState.asStateFlow()

    private val _releasesUiState: MutableStateFlow<ListUiState<ReleaseUi>> =
        MutableStateFlow(ListUiState.Loading)
    val releasesUiState = _releasesUiState.asStateFlow()

    private var languageJob: Job? = null
    private var releaseJob: Job? = null


    private val _items: StateFlow<List<RepoDetailItemUi>> =
        combine(
            _repoUiState,
            _languageUiState,
            _releasesUiState
        ) { repoDetail, language, releases ->
            val items = mutableListOf<RepoDetailItemUi>()

            when (repoDetail) {
                RepoDetailUiState.Loading -> {
                    items += RepoDetailItemUi.HeaderLoading
                }

                is RepoDetailUiState.Error -> {
                    items += RepoDetailItemUi.HeaderError("")
                }

                is RepoDetailUiState.Success -> {
                    with(repoDetail.data) {
                        items +=
                            RepoDetailItemUi.HeaderSuccess(
                                HeaderUi(
                                    owner.name,
                                    name,
                                    description.orEmpty()
                                )
                            )


                        items +=
                            RepoDetailItemUi.Stats(
                                StatsUi(
                                    stars,
                                    forks
                                )
                            )
                        items += RepoDetailItemUi.SectionTitle(UiText.StringRes(R.string.topics))

                        items += RepoDetailItemUi.Topics(topics)
                    }

                    items += RepoDetailItemUi.SectionTitle(UiText.StringRes(R.string.languages))

                    items += when (language) {
                        is ListUiState.Loading -> {
                            RepoDetailItemUi.LanguageLoading
                        }

                        is ListUiState.Error -> {
                            RepoDetailItemUi.LanguageError(
                                ApiErrorMapper.parseError(language.error)
                            )
                        }

                        is ListUiState.Success -> {
                            RepoDetailItemUi.LanguageSuccess(language.data)
                        }
                    }
                    items += RepoDetailItemUi.SectionTitle(UiText.StringRes(R.string.releases))

                    when (releases) {
                        is ListUiState.Error -> {
                            items += RepoDetailItemUi.ReleaseError(
                                ApiErrorMapper.parseError(
                                    releases.error
                                )
                            )
                        }

                        ListUiState.Loading -> {
                            items += RepoDetailItemUi.ReleaseLoading
                        }

                        is ListUiState.Success -> {
                            if (releases.data.isEmpty()) {
                                items += RepoDetailItemUi.NoReleases
                            } else
                                items += releases.data.map { release ->
                                    RepoDetailItemUi.ReleaseSuccess(release)
                                }

                        }
                    }

                }
            }



            items
        }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val uiItems: StateFlow<List<RepoDetailItemUi>> get() = _items


    init {
        loadRepoDetail(owner, repoName)
    }


    fun retryRepoDetail() = loadRepoDetail(owner, repoName)
    fun retryLanguages() = loadLanguages(owner, repoName)
    fun retryReleases() = loadReleases(owner, repoName)

    private fun loadRepoDetail(owner: String, repoName: String) {
        // Cancel dependent jobs
        languageJob?.cancel()
        releaseJob?.cancel()

        _repoUiState.value = RepoDetailUiState.Loading
        _languageUiState.value = ListUiState.Loading
        _releasesUiState.value = ListUiState.Loading

        viewModelScope.launch {

            when (val result = getRepoDetailUseCase(owner, repoName)) {
                is Result.Failure -> {
                    _repoUiState.value =
                        RepoDetailUiState.Error(ApiErrorMapper.parseError(result.error))
                }

                is Result.Success -> {
                    _repoUiState.value = RepoDetailUiState.Success(result.data.toRepoDetailUi())
                    coroutineScope {
                        launch { loadLanguages(owner, repoName) }
                        launch { loadReleases(owner, repoName) }
                    }
                }
            }

        }

    }

    private fun loadLanguages(owner: String, repoName: String) {
        languageJob?.cancel()
        languageJob = viewModelScope.launch {

            when (val languagesResult = getLanguageUseCase(owner, repoName)) {
                is Result.Failure -> _languageUiState.value =
                    ListUiState.Error(languagesResult.error)

                is Result.Success -> {
                    _languageUiState.value =
                        ListUiState.Success(languagesResult.data.toLanguageList())
                }
            }

        }
    }

    private fun loadReleases(ownerName: String, repoName: String) {
        releaseJob?.cancel()
        releaseJob = viewModelScope.launch {

            when (val releasesResult = getReleasesUseCase(ownerName, repoName)) {
                is Result.Success -> {
                    val formatter = RelativeTimeFormatter()
                    val releaseUis = releasesResult.data.map {
                        it.toReleaseUi(formatter)
                    }

                    _releasesUiState.value = ListUiState.Success(releaseUis)
                }

                is Result.Failure -> {
                    _releasesUiState.value = ListUiState.Error(DomainError.Unknown)
                }
            }

        }
    }


}