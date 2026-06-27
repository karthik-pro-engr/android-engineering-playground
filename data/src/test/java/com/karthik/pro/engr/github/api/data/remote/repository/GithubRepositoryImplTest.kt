@file:OptIn(ExperimentalCoroutinesApi::class)

package com.karthik.pro.engr.github.api.data.remote.repository

import androidx.paging.AsyncPagingDataDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import androidx.room.Room
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.karthik.pro.engr.github.api.core.testing.coroutine.MainDispatcherRule
import com.karthik.pro.engr.github.api.data.local.dao.RemoteKeysDao
import com.karthik.pro.engr.github.api.data.local.dao.RepoDao
import com.karthik.pro.engr.github.api.data.local.database.GithubDatabase
import com.karthik.pro.engr.github.api.data.remote.api.GithubService
import com.karthik.pro.engr.github.api.data.remote.error.ErrorParser
import com.karthik.pro.engr.github.api.domain.model.Repo
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class GithubRepositoryImplTest {

    private lateinit var repository: GithubRepositoryImpl
    private val service: GithubService = mockk()
    private val errorParser: ErrorParser = mockk()

    private val githubDatabase: GithubDatabase = mockk()
    private val repoDao: RepoDao = mockk()
    private val remoteKeysDao: RemoteKeysDao = mockk()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setup() {

        repository = GithubRepositoryImpl(
            service, errorParser,
            database = githubDatabase, repoDao = repoDao, remoteKeysDao = remoteKeysDao
        )
    }


    @Test
    fun `getUserRepos emits PagingData`() = runTest {
        val username = "testuser"

        coEvery { service.listUserRepos(any(), any(), any()) } returns Response.success(emptyList())

        repository.getUserRepos(username).test {
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

            assertThat(differ.snapshot()).isEmpty()
            job.cancel()

            cancelAndIgnoreRemainingEvents() // ✅ IMPORTANT
        }
    }


    class NoopListCallback : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onChanged(position: Int, count: Int, payload: Any?) {}
    }


}
