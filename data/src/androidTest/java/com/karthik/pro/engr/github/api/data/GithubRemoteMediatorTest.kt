package com.karthik.pro.engr.github.api.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingConfig
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.karthik.pro.engr.github.api.core.testing.coroutine.MainDispatcherRule
import com.karthik.pro.engr.github.api.data.local.dao.RemoteKeysDao
import com.karthik.pro.engr.github.api.data.local.dao.RepoDao
import com.karthik.pro.engr.github.api.data.local.database.GithubDatabase
import com.karthik.pro.engr.github.api.data.local.entity.RemoteKeysEntity
import com.karthik.pro.engr.github.api.data.local.entity.RepoEntity
import com.karthik.pro.engr.github.api.data.local.entity.RepoSearchEntity
import com.karthik.pro.engr.github.api.data.remote.api.GithubService
import com.karthik.pro.engr.github.api.data.remote.dto.error.ErrorDto
import com.karthik.pro.engr.github.api.data.remote.error.ErrorParser
import com.karthik.pro.engr.github.api.data.remote.mediator.GithubRemoteMediator
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import okhttp3.Headers
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Response
import java.io.IOException

@RunWith(AndroidJUnit4::class)
@OptIn(ExperimentalPagingApi::class)
class GithubRemoteMediatorTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var database: GithubDatabase

    private lateinit var repoDao: RepoDao

    private lateinit var remoteKeysDao: RemoteKeysDao

    private val service = mockk<GithubService>()

    private val errorParser = mockk<ErrorParser>()

    @Before
    fun setup() {

        database =
            Room.inMemoryDatabaseBuilder(
                ApplicationProvider.getApplicationContext(),
                GithubDatabase::class.java
            )
                .allowMainThreadQueries()
                .build()

        repoDao =
            database.repoDao()

        remoteKeysDao =
            database.remoteKeysDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun refresh_success_saves_search_user_and_repositories() =
        runTest {

            // Arrange

            val dto =
                GithubRepoDtoFactory.create()

            coEvery {
                service.listUserRepos(
                    any(),
                    any(),
                    any()
                )
            } returns Response.success(
                listOf(dto)
            )

            val mediator =
                remoteMediator(service)

            // Act

            val result =
                mediator.load(
                    LoadType.REFRESH,
                    pagingState()
                )

            // Assert

            assertThat(result)
                .isInstanceOf(
                    RemoteMediator.MediatorResult.Success::class.java
                )

            val searchUser =
                repoDao.findSearchUser(
                    USERNAME
                )

            assertThat(searchUser)
                .isNotNull()

            val repos =
                repoDao.getReposByUsername(
                    USERNAME
                )

            assertThat(repos)
                .hasSize(1)

            assertThat(
                repos.first().name
            ).isEqualTo(
                "compose-playground"
            )

            assertThat(
                remoteKeysDao.getNextKey(
                    USERNAME
                )
            ).isNull()
        }

    @Test
    fun refresh_api_failure_returns_error() = runTest {
        // Arrange

        val errorResponseBody =
            "Not Found"
                .toResponseBody(
                    "application/json".toMediaType()
                )

        coEvery {
            service.listUserRepos(
                any(),
                any(),
                any()
            )
        } returns Response.error(
            404,
            errorResponseBody
        )

        every {
            errorParser.parse(any())
        } returns ErrorDto(
            message = "User not found"
        )

        val mediator =
            remoteMediator(service)

        // Act

        val result =
            mediator.load(
                LoadType.REFRESH,
                pagingState()
            )

        // Assert
        assertThat(result)
            .isInstanceOf(
                RemoteMediator.MediatorResult.Error::class.java
            )

        assertThat(
            repoDao.findSearchUser(
                USERNAME
            )
        ).isNull()

        assertThat(
            repoDao.getReposByUsername(
                USERNAME
            )
        ).isEmpty()

        assertThat(
            remoteKeysDao.getNextKey(
                USERNAME
            )
        ).isNull()
    }

    @Test
    fun refresh_io_exception_returns_error() =
        runTest {

            coEvery {
                service.listUserRepos(
                    any(),
                    any(),
                    any()
                )
            } throws IOException(
                "No internet"
            )

            val mediator =
                remoteMediator(service)

            val result =
                mediator.load(
                    LoadType.REFRESH,
                    pagingState()
                )

            assertThat(result)
                .isInstanceOf(
                    RemoteMediator.MediatorResult.Error::class.java
                )

            val error =
                result as RemoteMediator.MediatorResult.Error

            assertThat(
                error.throwable.cause
            ).isInstanceOf(
                IOException::class.java
            )

            assertThat(
                repoDao.findSearchUser(
                    USERNAME
                )
            ).isNull()

            assertThat(
                repoDao.getReposByUsername(
                    USERNAME
                )
            ).isEmpty()

            assertThat(
                remoteKeysDao.getNextKey(
                    USERNAME
                )
            ).isNull()
        }

    @Test
    fun append_with_null_next_page_returns_end_of_pagination() =
        runTest {

            repoDao.upsertSearchUser(
                RepoSearchEntity(
                    username = USERNAME,
                    lastSync = 0,
                    lastAccessed = 0,
                    ownerId = 1L,
                    avatarUrl = null,
                    profileUrl = null
                )
            )

            remoteKeysDao.upsertNextKey(
                RemoteKeysEntity(
                    username = USERNAME,
                    nextPage = null
                )
            )

            val mediator =
                remoteMediator(service)

            val result =
                mediator.load(
                    LoadType.APPEND,
                    pagingState()
                )

            assertThat(result)
                .isInstanceOf(
                    RemoteMediator.MediatorResult.Success::class.java
                )

            val success =
                result as RemoteMediator.MediatorResult.Success

            assertThat(
                success.endOfPaginationReached
            ).isTrue()

            coVerify(exactly = 0) {

                service.listUserRepos(
                    any(),
                    any(),
                    any()
                )
            }
        }

    @Test
    fun append_success_appends_repositories_and_updates_remote_key() =
        runTest {

            // Arrange

            repoDao.upsertSearchUser(
                RepoSearchEntity(
                    username = USERNAME,
                    lastSync = 0,
                    lastAccessed = 0,
                    ownerId = 1L,
                    avatarUrl = null,
                    profileUrl = null
                )
            )

            remoteKeysDao.upsertNextKey(
                RemoteKeysEntity(
                    username = USERNAME,
                    nextPage = 2
                )
            )

            val dto =
                GithubRepoDtoFactory.create(
                    id = 100L,
                    name = "page-2-repo"
                )

            val headers =
                Headers.headersOf(
                    "Link",
                    """<https://api.github.com/user/repos?page=3>; rel="next""""
                )

            coEvery {
                service.listUserRepos(
                    any(),
                    any(),
                    any()
                )
            } returns Response.success(
                listOf(dto),
                headers
            )

            val mediator =
                remoteMediator(service)

            // Act

            val result =
                mediator.load(
                    LoadType.APPEND,
                    pagingState()
                )

            // Assert

            assertThat(result)
                .isInstanceOf(
                    RemoteMediator.MediatorResult.Success::class.java
                )

            val success =
                result as RemoteMediator.MediatorResult.Success

            assertThat(
                success.endOfPaginationReached
            ).isFalse()

            val repos =
                repoDao.getReposByUsername(
                    USERNAME
                )

            assertThat(repos)
                .hasSize(1)

            assertThat(
                repos.first().name
            ).isEqualTo(
                "page-2-repo"
            )

            assertThat(
                remoteKeysDao.getNextKey(
                    USERNAME
                )
            ).isEqualTo(
                3
            )

            coVerify(exactly = 1) {
                service.listUserRepos(
                    any(),
                    any(),
                    any()
                )
            }
        }

    @Test
    fun append_api_failure_returns_error() =
        runTest {

            // Arrange

            repoDao.upsertSearchUser(
                RepoSearchEntity(
                    username = USERNAME,
                    lastSync = 0,
                    lastAccessed = 0,
                    ownerId = 1L,
                    avatarUrl = null,
                    profileUrl = null
                )
            )

            remoteKeysDao.upsertNextKey(
                RemoteKeysEntity(
                    username = USERNAME,
                    nextPage = 2
                )
            )

            val errorBody =
                """
            {
                "message":"Not Found"
            }
            """.trimIndent()
                    .toResponseBody(
                        "application/json".toMediaType()
                    )

            coEvery {
                service.listUserRepos(
                    any(),
                    any(),
                    any()
                )
            } returns Response.error(
                404,
                errorBody
            )

            every {
                errorParser.parse(any())
            } returns ErrorDto(
                message = "Not Found"
            )

            val mediator =
                remoteMediator(service)

            // Act

            val result =
                mediator.load(
                    LoadType.APPEND,
                    pagingState()
                )

            // Assert

            assertThat(result)
                .isInstanceOf(
                    RemoteMediator.MediatorResult.Error::class.java
                )

            val repos =
                repoDao.getReposByUsername(
                    USERNAME
                )

            assertThat(repos)
                .isEmpty()

            assertThat(
                remoteKeysDao.getNextKey(
                    USERNAME
                )
            ).isEqualTo(
                2
            )

            coVerify(exactly = 1) {
                service.listUserRepos(
                    any(),
                    any(),
                    any()
                )
            }
        }

    @Test
    fun prepend_returns_end_of_pagination() =
        runTest {

            // Arrange

            val mediator =
                GithubRemoteMediator(
                    username = USERNAME,
                    service = service,
                    repoDao = repoDao,
                    database = database,
                    errorParser = errorParser,
                    remoteKeysDao = remoteKeysDao
                )

            // Act

            val result =
                mediator.load(
                    LoadType.PREPEND,
                    pagingState()
                )

            // Assert

            assertThat(result)
                .isInstanceOf(
                    RemoteMediator.MediatorResult.Success::class.java
                )

            val success =
                result as RemoteMediator.MediatorResult.Success

            assertThat(
                success.endOfPaginationReached
            ).isTrue()

            coVerify(exactly = 0) {
                service.listUserRepos(
                    any(),
                    any(),
                    any()
                )
            }
        }

    private fun pagingState() =
        PagingState<Int, RepoEntity>(
            pages = emptyList(),
            anchorPosition = null,
            config = PagingConfig(
                pageSize = 30
            ),
            leadingPlaceholderCount = 0
        )

    private fun remoteMediator(service: GithubService) = GithubRemoteMediator(
        username = USERNAME,
        service = service,
        repoDao = repoDao,
        database = database,
        errorParser = errorParser,
        remoteKeysDao = remoteKeysDao
    )

    companion object {

        private const val USERNAME =
            "karthik-pro-engr"
    }
}