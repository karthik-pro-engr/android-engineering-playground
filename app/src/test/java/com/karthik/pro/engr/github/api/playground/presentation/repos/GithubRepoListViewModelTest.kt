package com.karthik.pro.engr.github.api.playground.presentation.repos

import androidx.lifecycle.SavedStateHandle
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import app.cash.turbine.test
import com.karthik.pro.engr.github.api.core.testing.coroutine.MainDispatcherRule
import com.google.common.truth.Truth.assertThat
import com.karthik.pro.engr.github.api.core.testing.RepoFactory
import com.karthik.pro.engr.github.api.domain.model.Owner
import com.karthik.pro.engr.github.api.domain.model.Repo
import com.karthik.pro.engr.github.api.domain.usecase.GetUserReposUseCase
import io.mockk.Called
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GithubRepoListViewModelTest {

    private lateinit var useCase: GetUserReposUseCase
    private lateinit var savedStateHandle: SavedStateHandle
    private lateinit var viewModel: GithubReposListViewModel

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setup() {
        useCase = mockk()
        savedStateHandle = SavedStateHandle()

        viewModel = GithubReposListViewModel(
            useCase = useCase,
            savedStateHandle = savedStateHandle
        )

    }

    @Test
    fun `submitQuery trims whitespace`() {
        viewModel.submitQuery("  JakeWharton  ")
        assertThat(viewModel.currentQuery.value).isEqualTo("JakeWharton")
    }

    @Test
    fun `submitQuery converts blank input to null`() {
        viewModel.submitQuery("        ")
        assertThat(viewModel.currentQuery.value).isNull()
    }


    @Test
    fun `submitQuery saves trimmed query into SavedStateHandle`() = runTest {
        viewModel.submitQuery("  JakeWharton  ")

        advanceUntilIdle()

        val value1 = savedStateHandle["last_user_name"] as String?

        assertThat(value1 ?: "").isEqualTo("JakeWharton")
    }

    @Test
    fun `reposSharedFlow calls useCase when valid query submitted`() = runTest {

        coEvery { useCase("JakeWharton") } returns flowOf(PagingData.empty())

        viewModel.submitQuery("JakeWharton")

        viewModel.reposSharedFlow.test {
            awaitItem()
            coVerify(exactly = 1) {
                useCase("JakeWharton")
            }
            cancelAndIgnoreRemainingEvents()
        }

    }

    @Test
    fun `repoSharedFlow won't make only one call when submit same query twice`() = runTest {

        coEvery { useCase("JakeWharton") } returns flowOf(PagingData.empty())

        viewModel.submitQuery("JakeWharton")
        viewModel.submitQuery("JakeWharton")

        viewModel.reposSharedFlow.test {
            awaitItem()

            coVerify(exactly = 1) {
                useCase("JakeWharton")
            }
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `null query should not trigger useCase`() = runTest {

        viewModel.submitQuery("    ")

        val job = launch {
            viewModel.reposSharedFlow.collect {}
        }

        coVerify { useCase wasNot Called }

        advanceUntilIdle()

        job.cancel()

    }

    @Test
    fun `new query cancels previous request and calls useCase only for latest`() = runTest {

        val flowFirst = flow<PagingData<Repo>> {
            delay(100)
            emit(PagingData.empty())
        }

        val flowSecond = flowOf(
            PagingData.from(
                listOf(
                    RepoFactory.withId( 1069192744),
                    RepoFactory.withId( 1069192745),
                )
            )
        )

        coEvery { useCase("Karthik") } returns flowFirst

        coEvery { useCase("karthik-pro-engr") } returns flowSecond

        viewModel.submitQuery("karthik")

        runCurrent()

        viewModel.submitQuery("karthik-pro-engr")

        viewModel.reposSharedFlow.test {
            val pagingData = awaitItem()

            val differ = AsyncPagingDataDiffer(
                diffCallback = object : DiffUtil.ItemCallback<Repo>() {
                    override fun areItemsTheSame(oldItem: Repo, newItem: Repo) =
                        oldItem.id == newItem.id

                    override fun areContentsTheSame(oldItem: Repo, newItem: Repo) =
                        oldItem == newItem
                },
                updateCallback = NoopListCallback(),
                workerDispatcher = StandardTestDispatcher(testScheduler)
            )

            val job = launch {
                differ.submitData(pagingData)
            }
            advanceUntilIdle()

            assertThat(2).isEqualTo(differ.itemCount)
            assertThat(1069192745).isEqualTo(differ.getItem(1)?.id)

            coVerify(exactly = 0) { useCase("karthik") }
            coVerify(exactly = 1) { useCase("karthik-pro-engr") }
            job.cancel()
            cancelAndIgnoreRemainingEvents()

        }

    }

    class NoopListCallback : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onChanged(position: Int, count: Int, payload: Any?) {}
    }

}