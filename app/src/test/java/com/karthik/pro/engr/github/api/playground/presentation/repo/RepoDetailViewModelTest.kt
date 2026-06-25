package com.karthik.pro.engr.github.api.playground.presentation.repo

import app.cash.turbine.ReceiveTurbine
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.karthik.pro.engr.github.api.core.testing.RepoFactory
import com.karthik.pro.engr.github.api.data.remote.mapper.toLanguageList
import com.karthik.pro.engr.github.api.domain.error.DomainError
import com.karthik.pro.engr.github.api.domain.model.Repo
import com.karthik.pro.engr.github.api.playground.presentation.repo.model.RepoDetailItemUi
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import com.karthik.pro.engr.github.api.domain.result.Result
import io.mockk.coVerify
import kotlinx.coroutines.test.advanceUntilIdle

private suspend fun <T> ReceiveTurbine<T>.awaitUntil(
    condition: (T) -> Boolean
): T {
    while (true) {
        val item = awaitItem()
        if (condition(item)) return item
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class RepoDetailViewModelTest {
/*
    private val repoUseCase = mockk<GetRepoDetailUseCase>()
    private val languageUseCase = mockk<GetLanguageUseCase>()
    private val releaseUseCase = mockk<GetReleasesUseCase>()

    private fun createViewModel() = RepoDetailViewModel(
        savedStateHandle = SavedStateHandleFactory.repoDetail(),
        repoUseCase,
        languageUseCase,
        releaseUseCase
    )

    private fun successRepo() =
        Result.Success(RepoFactory.defaultRepo())

    private fun successLanguages() =
        Result.Success(RepoFactory.defaultLanguages())

    private fun successReleases() =
        Result.Success(RepoFactory.defaultReleases())

    private fun failureUnknown() = Result.Failure(DomainError.Unknown)

    @Test
    fun `when repo loading then show header loading`() = runTest {
        coEvery { repoUseCase(any(), any()) } coAnswers {
            awaitCancellation()
        }

        val vm = createViewModel()

        vm.uiItems.test {
            val items = awaitUntil {
                it.firstOrNull() is RepoDetailItemUi.HeaderLoading
            }

            assertThat(items.first())
                .isInstanceOf(RepoDetailItemUi.HeaderLoading::class.java)

            cancelAndIgnoreRemainingEvents()
        }
    }


    @Test
    fun `when repo error then show only header error`() = runTest {
        coEvery { repoUseCase(any(), any()) } returns Result.Failure(DomainError.Unknown)

        val vm = createViewModel()

        vm.uiItems.test {
            val items = awaitUntil {
                it.firstOrNull() is RepoDetailItemUi.HeaderError
            }

            assertThat(items.size).isEqualTo(1)
            assertThat(items.first())
                .isInstanceOf(RepoDetailItemUi.HeaderError::class.java)

            cancelAndIgnoreRemainingEvents()
        }
    }


    @Test
    fun `when repo success and others loading then show loading sections`() = runTest {
        coEvery { repoUseCase(any(), any()) } returns successRepo()
        coEvery { languageUseCase(any(), any()) } coAnswers { awaitCancellation() }
        coEvery { releaseUseCase(any(), any()) } coAnswers { awaitCancellation() }

        val vm = createViewModel()

        vm.uiItems.test {
            val items = awaitUntil {
                it.any { item -> item is RepoDetailItemUi.LanguageLoading } &&
                        it.any { item -> item is RepoDetailItemUi.ReleaseLoading }
            }

            assertThat(items.first())
                .isInstanceOf(RepoDetailItemUi.HeaderSuccess::class.java)

            assertThat(items.any { it is RepoDetailItemUi.LanguageLoading }).isTrue()
            assertThat(items.any { it is RepoDetailItemUi.ReleaseLoading }).isTrue()

            cancelAndIgnoreRemainingEvents()
        }
    }


    @Test
    fun `when language fails then show language error`() = runTest {
        coEvery { repoUseCase(any(), any()) } returns successRepo()
        coEvery { languageUseCase(any(), any()) } returns failureUnknown()
        coEvery { releaseUseCase(any(), any()) } coAnswers { awaitCancellation() }

        val vm = createViewModel()

        vm.uiItems.test {
            val items = awaitUntil {
                it.any { item -> item is RepoDetailItemUi.LanguageError }
            }

            assertThat(items.any { it is RepoDetailItemUi.LanguageError }).isTrue()

            cancelAndIgnoreRemainingEvents()
        }
    }

    // ----------------------------
    // Release Error
    // ----------------------------
    @Test
    fun `when release fails then show release error`() = runTest {
        coEvery { repoUseCase(any(), any()) } returns successRepo()
        coEvery { languageUseCase(any(), any()) } coAnswers { awaitCancellation() }
        coEvery { releaseUseCase(any(), any()) } returns failureUnknown()
        val vm = createViewModel()

        vm.uiItems.test {
            val items = awaitUntil {
                it.any { item -> item is RepoDetailItemUi.ReleaseError }
            }

            assertThat(items.any { it is RepoDetailItemUi.ReleaseError }).isTrue()

            cancelAndIgnoreRemainingEvents()
        }
    }


    @Test
    fun `when all success then show full ui`() = runTest {
        coEvery { repoUseCase(any(), any()) } returns successRepo()
        coEvery { languageUseCase(any(), any()) } returns successLanguages()
        coEvery { releaseUseCase(any(), any()) } returns successReleases()

        val vm = createViewModel()

        vm.uiItems.test {
            val items = awaitUntil {
                it.any { item -> item is RepoDetailItemUi.LanguageSuccess } &&
                        it.any { item -> item is RepoDetailItemUi.ReleaseSuccess }
            }

            assertThat(items.any { it is RepoDetailItemUi.LanguageSuccess }).isTrue()
            assertThat(items.any { it is RepoDetailItemUi.ReleaseSuccess }).isTrue()

            cancelAndIgnoreRemainingEvents()
        }
    }


    @Test
    fun `when retry repo then repo use case invoked again`() = runTest {

        coEvery { repoUseCase(any(), any()) } returns successRepo()

        coEvery { languageUseCase(any(), any()) } returns Result.Success(
            RepoFactory.defaultLanguages()
        )

        coEvery { releaseUseCase(any(), any()) } returns Result.Success(
            RepoFactory.defaultReleases()
        )

        val vm = createViewModel()

        advanceUntilIdle()

        vm.retryRepoDetail()

        advanceUntilIdle()

        coVerify(exactly = 2) {
            repoUseCase(any(), any())
        }
    }*/
}