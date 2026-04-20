package com.karthik.pro.engr.github.api.playground.presentation.repo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.karthik.pro.engr.github.api.domain.error.DomainError
import com.karthik.pro.engr.github.api.domain.model.Language
import com.karthik.pro.engr.github.api.domain.usecase.GetLanguageUseCase
import com.karthik.pro.engr.github.api.domain.usecase.GetReleasesUseCase
import com.karthik.pro.engr.github.api.domain.usecase.GetRepoDetailUseCase
import com.karthik.pro.engr.github.api.playground.R
import com.karthik.pro.engr.github.api.playground.presentation.common.UiText
import com.karthik.pro.engr.github.api.playground.presentation.common.formatter.RelativeTimeFormatter
import com.karthik.pro.engr.github.api.playground.presentation.error.ApiErrorMapper
import com.karthik.pro.engr.github.api.playground.presentation.repo.components.header.model.HeaderUi
import com.karthik.pro.engr.github.api.playground.presentation.repo.components.releases.model.ReleaseUi
import com.karthik.pro.engr.github.api.playground.presentation.repo.components.stats.StatsUi
import com.karthik.pro.engr.github.api.playground.presentation.repo.mapper.toReleaseUi
import com.karthik.pro.engr.github.api.playground.presentation.repo.mapper.toRepoDetailUi
import com.karthik.pro.engr.github.api.playground.presentation.repo.model.RepoDetailItemUi
import com.karthik.pro.engr.github.api.playground.presentation.uistate.ListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RepoDetailViewModel @Inject constructor(
    private val getRepoDetailUseCase: GetRepoDetailUseCase,
    private val getLanguageUseCase: GetLanguageUseCase,
    private val getReleasesUseCase: GetReleasesUseCase
) : ViewModel() {

    private var _repoUiState = MutableStateFlow<RepoDetailUiState>(RepoDetailUiState.Loading)
    val repoUiState = _repoUiState.asStateFlow()

    private var _languageUiState = MutableStateFlow<ListUiState<Language>>(ListUiState.Loading)
    val languageUiState = _languageUiState.asStateFlow()

    private val _releasesUiState: MutableStateFlow<ListUiState<ReleaseUi>> =
        MutableStateFlow(ListUiState.Loading)
    val releasesUiState = _releasesUiState.asStateFlow()

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
                        items+= RepoDetailItemUi.SectionTitle(UiText.StringRes(R.string.topics))

                        items += RepoDetailItemUi.Topics(topics)
                    }

                }
            }

            items+= RepoDetailItemUi.SectionTitle(UiText.StringRes(R.string.languages))

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
            items+= RepoDetailItemUi.SectionTitle(UiText.StringRes(R.string.releases))

            when (releases) {
                is ListUiState.Error -> {
                    items += RepoDetailItemUi.ReleaseError(ApiErrorMapper.parseError(releases.error))
                }

                ListUiState.Loading -> {
                    items += RepoDetailItemUi.ReleaseLoading
                }

                is ListUiState.Success -> {
                    items +=
                        releases.data.map { release ->
                            RepoDetailItemUi.ReleaseSuccess(release)
                        }

                }
            }

            items
        }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val uiItems: StateFlow<List<RepoDetailItemUi>> get() = _items


    init {
        loadRepoDetail("", "")
    }


    fun retryRepoDetail(owner: String, repoName: String) = loadRepoDetail(owner, repoName)
    fun retryLanguages(owner: String, repoName: String) = loadLanguages(owner, repoName)
    fun retryReleases(owner: String, repoName: String) = loadReleases(owner, repoName)

    private fun loadRepoDetail(owner: String, repoName: String) {
        viewModelScope.launch {
            try {

                val repo = getRepoDetailUseCase(owner, repoName).first()
                _repoUiState.value = RepoDetailUiState.Success(repo.toRepoDetailUi())

                loadLanguages("", "")
                loadReleases("", "")
            } catch (e: Exception) {
                _repoUiState.value = RepoDetailUiState.Error("")
            }

        }

    }

    private fun loadLanguages(owner: String, repoName: String) {
        viewModelScope.launch {
            try {

                val languages = getLanguageUseCase(owner, repoName).first()

                _languageUiState.value = ListUiState.Success(languages)

            } catch (e: Exception) {
                _languageUiState.value = ListUiState.Error(DomainError.Unknown)
            }
        }
    }

    private fun loadReleases(ownerName: String, repoName: String) {

        viewModelScope.launch {
            try {
                val releases = getReleasesUseCase(ownerName, repoName).first()

                val formatter = RelativeTimeFormatter()
                val releaseUis = releases.map {
                    it.toReleaseUi(formatter)
                }

                _releasesUiState.value = ListUiState.Success(releaseUis)

            } catch (e: Exception) {
                ListUiState.Error(DomainError.Unknown)
            }
        }
    }


}