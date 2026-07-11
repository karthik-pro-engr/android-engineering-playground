package com.karthik.pro.engr.github.api.playground.presentation.repo

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.karthik.pro.engr.github.api.domain.model.Language
import com.karthik.pro.engr.github.api.domain.result.Result
import com.karthik.pro.engr.github.api.domain.usecase.ObserveLanguagesUseCase
import com.karthik.pro.engr.github.api.domain.usecase.ObserveReleasesUseCase
import com.karthik.pro.engr.github.api.domain.usecase.ObserveRepoDetailUseCase
import com.karthik.pro.engr.github.api.domain.usecase.RefreshLanguagesUseCase
import com.karthik.pro.engr.github.api.domain.usecase.RefreshRepoDetailUseCase
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

    private val observeRepoDetailUseCase: ObserveRepoDetailUseCase,
    private val refreshRepoDetailUseCase: RefreshRepoDetailUseCase,
    private val observeLanguagesUseCase: ObserveLanguagesUseCase,
    private val refreshLanguagesUseCase: RefreshLanguagesUseCase,
    private val observeReleasesUseCase: ObserveReleasesUseCase
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

    private var languageObserveJob: Job? = null
    private var releaseObserveJob: Job? = null

    private val _showStaleDataBanner =
        MutableStateFlow(false)

    val showStaleDataBanner =
        _showStaleDataBanner.asStateFlow()

    private var repoRefreshFailed = false

    private var languageRefreshFailed = false

    private var releaseRefreshFailed = false

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
                                    ownerName,
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
        observeRepoDetail()
        refreshRepoDetail()
    }


   fun retryRepoDetail() {
    refreshRepoDetail()
    refreshLanguages()
}
    fun retryLanguages() {
        refreshLanguages()
    }

    fun retryReleases() {
        refreshRepoDetail()
    }

    private fun observeRepoDetail() {
        _repoUiState.value = RepoDetailUiState.Loading
        _languageUiState.value = ListUiState.Loading
        _releasesUiState.value = ListUiState.Loading

        viewModelScope.launch {

            observeRepoDetailUseCase(
                owner,
                repoName
            ).collect { repo ->
                Log.d("REpoDEtailViemodel", "observeRepoDetail: $repo")
                if (repo == null) return@collect

                _repoUiState.value =
                    RepoDetailUiState.Success(
                        repo.toRepoDetailUi()
                    )

                if (languageObserveJob?.isActive != true) {

                    observeLanguages()

                    refreshLanguages()
                }

                if (releaseObserveJob?.isActive != true) {

                    observeReleases()

                }

            }
        }
    }

    private fun refreshRepoDetail() {

        viewModelScope.launch {

            when (
                val result = refreshRepoDetailUseCase(
                    owner,
                    repoName
                )
            ) {

                is Result.Success -> {
                    Log.d("REpoDEtailViemodel", "refreshRepoDetail:Result.Success ")
                    repoRefreshFailed = false

                    updateOfflineBanner()
                }

                is Result.Failure -> {


                    if (_repoUiState.value !is RepoDetailUiState.Success) {
                        Log.d("REpoDEtailViemodel", "refreshRepoDetail:Result.Failure if block ")


                        _repoUiState.value =
                            RepoDetailUiState.Error(
                                ApiErrorMapper.parseError(
                                    result.error
                                )
                            )
                    }else {
                        Log.d("REpoDEtailViemodel", "refreshRepoDetail:Result.Failure else block ")

                        repoRefreshFailed = true

                        updateOfflineBanner()
                    }
                }
            }
        }
    }

    private fun observeLanguages() {

        languageObserveJob = viewModelScope.launch {

            observeLanguagesUseCase(
                owner,
                repoName
            ).collect { languages ->
                Log.d("REpoDEtailViemodel", "observeLanguages: $languages ")
                if (
                    languages.isEmpty() &&
                    _languageUiState.value is ListUiState.Loading
                ) {
                    return@collect
                }

                _languageUiState.value =
                    ListUiState.Success(languages)
            }
        }
    }

    private fun refreshLanguages() {

        viewModelScope.launch {

            when (
                val result = refreshLanguagesUseCase(
                    owner,
                    repoName
                )
            ) {

                is Result.Success -> {
                    Log.d("REpoDEtailViemodel", "refreshLanguages:Result.Success ")

                    languageRefreshFailed = false

                    updateOfflineBanner()
                }

                is Result.Failure -> {

                    if (
                        _languageUiState.value
                                !is ListUiState.Success
                    ) {
                        Log.d("REpoDEtailViemodel", "refreshLanguages:Result.Failure if")


                        _languageUiState.value =
                            ListUiState.Error(
                                result.error
                            )
                    }else {
                        Log.d("REpoDEtailViemodel", "refreshLanguages:Result.Failure else")

                        languageRefreshFailed = true

                        updateOfflineBanner()
                    }
                }
            }
        }
    }

    private fun observeReleases() {

        releaseObserveJob = viewModelScope.launch {

            observeReleasesUseCase(
                owner,
                repoName
            ).collect { releases ->

                Log.d("REpoDEtailViemodel", "observeReleases: $releases")

                if (
                    releases.isEmpty() &&
                    _languageUiState.value is ListUiState.Loading
                ) {
                    return@collect
                }

                val formatter =
                    RelativeTimeFormatter()

                _releasesUiState.value =
                    ListUiState.Success(
                        releases.map {
                            it.toReleaseUi(
                                formatter
                            )
                        }
                    )
            }
        }
    }



    private fun updateOfflineBanner() {

        _showStaleDataBanner.value =
            repoRefreshFailed ||
                    languageRefreshFailed ||
                    releaseRefreshFailed
    }

}