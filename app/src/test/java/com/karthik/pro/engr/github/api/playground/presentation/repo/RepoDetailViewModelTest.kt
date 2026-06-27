package com.karthik.pro.engr.github.api.playground.presentation.repo

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.ReceiveTurbine
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.karthik.pro.engr.github.api.core.testing.RepoFactory
import com.karthik.pro.engr.github.api.core.testing.coroutine.MainDispatcherRule
import com.karthik.pro.engr.github.api.data.remote.mapper.toLanguageList
import com.karthik.pro.engr.github.api.domain.error.DomainError
import com.karthik.pro.engr.github.api.domain.model.Language
import com.karthik.pro.engr.github.api.domain.model.Release
import com.karthik.pro.engr.github.api.domain.model.Repo
import com.karthik.pro.engr.github.api.domain.result.Result
import com.karthik.pro.engr.github.api.domain.usecase.ObserveLanguagesUseCase
import com.karthik.pro.engr.github.api.domain.usecase.ObserveReleasesUseCase
import com.karthik.pro.engr.github.api.domain.usecase.ObserveRepoDetailUseCase
import com.karthik.pro.engr.github.api.domain.usecase.RefreshLanguagesUseCase
import com.karthik.pro.engr.github.api.domain.usecase.RefreshReleasesUseCase
import com.karthik.pro.engr.github.api.domain.usecase.RefreshRepoDetailUseCase
import com.karthik.pro.engr.github.api.playground.presentation.navigation.AppDestinations.REPO_NAME_ARG
import com.karthik.pro.engr.github.api.playground.presentation.navigation.AppDestinations.REPO_OWNER_ARG
import com.karthik.pro.engr.github.api.playground.presentation.repo.mapper.toRepoDetailUi
import com.karthik.pro.engr.github.api.playground.presentation.repo.model.RepoDetailItemUi
import io.mockk.MockKAnnotations
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RepoDetailViewModelTest {

    @get:Rule
    val mainDispatcherRule =
        MainDispatcherRule()

    @MockK
    private lateinit var observeRepoDetailUseCase: ObserveRepoDetailUseCase

    @MockK
    private lateinit var refreshRepoDetailUseCase: RefreshRepoDetailUseCase

    @MockK
    private lateinit var observeLanguagesUseCase: ObserveLanguagesUseCase

    @MockK
    private lateinit var refreshLanguagesUseCase: RefreshLanguagesUseCase

    @MockK
    private lateinit var observeReleasesUseCase: ObserveReleasesUseCase

    @MockK
    private lateinit var refreshReleasesUseCase: RefreshReleasesUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        mockkStatic(Log::class)
        every {
            Log.d(
                any(),
                any()
            )
        } returns 0
    }

    @After
    fun tearDown() {
        unmockkStatic(Log::class)
    }

    @Test
    fun cached_repo_and_refresh_failure_shows_stale_banner() =
        runTest {
            // Arrange
            arrangeObservedData(
                repo = RepoFactory.defaultRepo()
            )
            arrangeRefreshes(
                repoResult = Result.Failure(
                    DomainError.Network
                )
            )

            // Act
            val viewModel =
                createViewModel()

            advanceUntilIdle()

            // Assert
            viewModel.showStaleDataBanner.test {
                assertThat(awaitItem())
                    .isTrue()

                assertThat(
                    viewModel.showStaleDataBanner.value
                ).isTrue()

                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun no_cache_and_refresh_failure_shows_repo_error() =
        runTest {
            // Arrange
            arrangeObservedData(
                repo = null
            )
            arrangeRefreshes(
                repoResult = Result.Failure(
                    DomainError.Network
                )
            )

            // Act
            val viewModel =
                createViewModel()

            advanceUntilIdle()

            // Assert
            viewModel.repoUiState.test {
                val state =
                    awaitItem()

                assertThat(state)
                    .isInstanceOf(
                        RepoDetailUiState.Error::class.java
                    )

                assertThat(
                    viewModel.repoUiState.value
                ).isEqualTo(
                    state
                )

                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun observed_repo_updates_repo_ui_state() =
        runTest {
            // Arrange
            val repo =
                RepoFactory.defaultRepo()

            arrangeObservedData(
                repo = repo
            )
            arrangeRefreshes()

            // Act
            val viewModel =
                createViewModel()

            advanceUntilIdle()

            // Assert
            viewModel.repoUiState.test {
                assertThat(awaitItem())
                    .isEqualTo(
                        RepoDetailUiState.Success(
                            repo.toRepoDetailUi()
                        )
                    )

                assertThat(
                    viewModel.repoUiState.value
                ).isEqualTo(
                    RepoDetailUiState.Success(
                        repo.toRepoDetailUi()
                    )
                )

                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun successful_load_builds_expected_ui_items() =
        runTest {
            // Arrange
            arrangeObservedData(
                repo = RepoFactory.defaultRepo(),
                languages = defaultLanguages(),
                releases = RepoFactory.defaultReleases()
            )
            arrangeRefreshes()

            // Act
            val viewModel =
                createViewModel()

            // Assert
            viewModel.uiItems.test {
                advanceUntilIdle()

                val items =
                    awaitUntil { uiItems ->
                        uiItems.any { it is RepoDetailItemUi.HeaderSuccess } &&
                                uiItems.any { it is RepoDetailItemUi.Stats } &&
                                uiItems.any { it is RepoDetailItemUi.Topics } &&
                                uiItems.any { it is RepoDetailItemUi.LanguageSuccess } &&
                                uiItems.any { it is RepoDetailItemUi.ReleaseSuccess }
                    }

                assertThat(
                    items.any { it is RepoDetailItemUi.HeaderSuccess }
                ).isTrue()
                assertThat(
                    items.any { it is RepoDetailItemUi.Stats }
                ).isTrue()
                assertThat(
                    items.any { it is RepoDetailItemUi.Topics }
                ).isTrue()
                assertThat(
                    items.any { it is RepoDetailItemUi.LanguageSuccess }
                ).isTrue()
                assertThat(
                    items.any { it is RepoDetailItemUi.ReleaseSuccess }
                ).isTrue()

                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun empty_releases_shows_no_releases_item() =
        runTest {
            // Arrange
            arrangeObservedData(
                repo = RepoFactory.defaultRepo(),
                languages = defaultLanguages(),
                releases = emptyList()
            )
            arrangeRefreshes()

            // Act
            val viewModel =
                createViewModel()

            // Assert
            viewModel.uiItems.test {
                advanceUntilIdle()

                val items =
                    awaitUntil { uiItems ->
                        uiItems.any { it is RepoDetailItemUi.NoReleases }
                    }

                assertThat(
                    items.any { it is RepoDetailItemUi.NoReleases }
                ).isTrue()

                assertThat(
                    viewModel.uiItems.value
                ).contains(
                    RepoDetailItemUi.NoReleases
                )

                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun retry_repo_detail_invokes_all_refresh_use_cases() =
        runTest {
            // Arrange
            arrangeObservedData(
                repo = null
            )
            arrangeRefreshes()

            val viewModel =
                createViewModel()

            advanceUntilIdle()

            clearMocks(
                refreshRepoDetailUseCase,
                refreshLanguagesUseCase,
                refreshReleasesUseCase,
                answers = false
            )

            // Act
            viewModel.retryRepoDetail()

            advanceUntilIdle()

            // Assert
            coVerify(exactly = 1) {
                refreshRepoDetailUseCase(
                    OWNER_NAME,
                    REPO_NAME
                )
            }
            coVerify(exactly = 1) {
                refreshLanguagesUseCase(
                    OWNER_NAME,
                    REPO_NAME
                )
            }
            coVerify(exactly = 1) {
                refreshReleasesUseCase(
                    OWNER_NAME,
                    REPO_NAME
                )
            }
        }

    @Test
    fun retry_languages_invokes_refresh_languages_use_case() =
        runTest {
            // Arrange
            arrangeObservedData(
                repo = null
            )
            arrangeRefreshes()

            val viewModel =
                createViewModel()

            advanceUntilIdle()

            clearMocks(
                refreshLanguagesUseCase,
                answers = false
            )

            // Act
            viewModel.retryLanguages()

            advanceUntilIdle()

            // Assert
            coVerify(exactly = 1) {
                refreshLanguagesUseCase(
                    OWNER_NAME,
                    REPO_NAME
                )
            }
        }

    @Test
    fun retry_releases_invokes_refresh_releases_use_case() =
        runTest {
            // Arrange
            arrangeObservedData(
                repo = null
            )
            arrangeRefreshes()

            val viewModel =
                createViewModel()

            advanceUntilIdle()

            clearMocks(
                refreshReleasesUseCase,
                answers = false
            )

            // Act
            viewModel.retryReleases()

            advanceUntilIdle()

            // Assert
            coVerify(exactly = 1) {
                refreshReleasesUseCase(
                    OWNER_NAME,
                    REPO_NAME
                )
            }
        }

    private fun createViewModel(): RepoDetailViewModel {
        return RepoDetailViewModel(
            savedStateHandle = SavedStateHandle(
                mapOf(
                    REPO_OWNER_ARG to OWNER_NAME,
                    REPO_NAME_ARG to REPO_NAME
                )
            ),
            observeRepoDetailUseCase = observeRepoDetailUseCase,
            refreshRepoDetailUseCase = refreshRepoDetailUseCase,
            observeLanguagesUseCase = observeLanguagesUseCase,
            refreshLanguagesUseCase = refreshLanguagesUseCase,
            observeReleasesUseCase = observeReleasesUseCase,
            refreshReleasesUseCase = refreshReleasesUseCase
        )
    }

    private fun arrangeObservedData(
        repo: Repo?,
        languages: List<Language> = defaultLanguages(),
        releases: List<Release> = RepoFactory.defaultReleases()
    ) {
        every {
            observeRepoDetailUseCase(
                OWNER_NAME,
                REPO_NAME
            )
        } returns flowOf(repo)

        every {
            observeLanguagesUseCase(
                OWNER_NAME,
                REPO_NAME
            )
        } returns flowOf(languages)

        every {
            observeReleasesUseCase(
                OWNER_NAME,
                REPO_NAME
            )
        } returns flowOf(releases)
    }

    private fun arrangeRefreshes(
        repoResult: Result<Unit, DomainError> = Result.Success(Unit),
        languagesResult: Result<Unit, DomainError> = Result.Success(Unit),
        releasesResult: Result<Unit, DomainError> = Result.Success(Unit)
    ) {
        coEvery {
            refreshRepoDetailUseCase(
                OWNER_NAME,
                REPO_NAME
            )
        } returns repoResult

        coEvery {
            refreshLanguagesUseCase(
                OWNER_NAME,
                REPO_NAME
            )
        } returns languagesResult

        coEvery {
            refreshReleasesUseCase(
                OWNER_NAME,
                REPO_NAME
            )
        } returns releasesResult
    }

    private fun defaultLanguages(): List<Language> {
        return RepoFactory.defaultLanguages()
            .toLanguageList()
    }

    private suspend fun <T> ReceiveTurbine<T>.awaitUntil(
        condition: (T) -> Boolean
    ): T {
        while (true) {
            val item =
                awaitItem()

            if (condition(item)) {
                return item
            }
        }
    }

    private companion object {
        const val OWNER_NAME = "karthik-pro-engr"
        const val REPO_NAME = "github-api-playground"
    }
}
