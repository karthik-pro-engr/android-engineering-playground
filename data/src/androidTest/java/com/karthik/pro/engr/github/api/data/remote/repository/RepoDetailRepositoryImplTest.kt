package com.karthik.pro.engr.github.api.data.remote.repository

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.karthik.pro.engr.github.api.data.GithubRepoDtoFactory
import com.karthik.pro.engr.github.api.data.local.dao.RepoDao
import com.karthik.pro.engr.github.api.data.local.dao.RepoLanguageDao
import com.karthik.pro.engr.github.api.data.local.dao.RepoReleaseDao
import com.karthik.pro.engr.github.api.data.local.database.GithubDatabase
import com.karthik.pro.engr.github.api.data.local.entity.RepoEntity
import com.karthik.pro.engr.github.api.data.local.entity.RepoLanguageEntity
import com.karthik.pro.engr.github.api.data.local.entity.RepoReleaseEntity
import com.karthik.pro.engr.github.api.data.local.entity.RepoSearchEntity
import com.karthik.pro.engr.github.api.data.remote.api.GithubService
import com.karthik.pro.engr.github.api.data.remote.dto.response.ReleaseDto
import com.karthik.pro.engr.github.api.data.remote.error.ErrorParser
import com.karthik.pro.engr.github.api.data.remote.mapper.ReleaseMapper
import com.karthik.pro.engr.github.api.data.remote.mapper.RepoDomainMapper
import com.karthik.pro.engr.github.api.data.remote.mapper.RepoLanguageMapper
import com.karthik.pro.engr.github.api.data.remote.mapper.RepoReleaseMapper
import com.karthik.pro.engr.github.api.domain.error.DomainError
import com.karthik.pro.engr.github.api.domain.result.Result
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@RunWith(AndroidJUnit4::class)
@OptIn(ExperimentalCoroutinesApi::class)
class RepoDetailRepositoryImplTest {

    private lateinit var database: GithubDatabase
    private lateinit var repoDao: RepoDao
    private lateinit var repoLanguageDao: RepoLanguageDao
    private lateinit var repoReleaseDao: RepoReleaseDao

    private val service = mockk<GithubService>()

    private val errorParser =
        ErrorParser(
            Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        )

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

        repoLanguageDao =
            database.repoLanguageDao()

        repoReleaseDao =
            database.repoReleaseDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun refreshRepoDetail_success_persists_and_observe_emits_repo() =
        runTest {
            // Arrange
            val repository =
                repository(
                    StandardTestDispatcher(
                        testScheduler
                    )
                )

            val dto =
                GithubRepoDtoFactory.create(
                    id = REPO_ID,
                    name = REPO_NAME,
                    ownerName = OWNER_NAME
                )

            coEvery {
                service.repoDetail(
                    OWNER_NAME,
                    REPO_NAME
                )
            } returns dto

            // Act
            val result =
                repository.refreshRepoDetail(
                    OWNER_NAME,
                    REPO_NAME
                )

            // Assert
            assertThat(result)
                .isEqualTo(
                    Result.Success(Unit)
                )

            repository.observeRepoDetail(
                OWNER_NAME,
                REPO_NAME
            ).test {
                assertThat(awaitItem())
                    .isEqualTo(
                        RepoDomainMapper.fromEntity(
                            repoEntity()
                        )
                    )

                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun refreshLanguages_success_persists_and_observe_emits_languages() =
        runTest {
            // Arrange
            insertRepoSearchEntity()
            insertRepoEntity()

            val repository =
                repository(
                    StandardTestDispatcher(
                        testScheduler
                    )
                )

            val languages =
                linkedMapOf(
                    "Kotlin" to 300L,
                    "Java" to 100L
                )

            coEvery {
                service.getRepoLanguages(
                    OWNER_NAME,
                    REPO_NAME
                )
            } returns languages

            // Act
            val result =
                repository.refreshLanguages(
                    OWNER_NAME,
                    REPO_NAME
                )

            // Assert
            assertThat(result)
                .isEqualTo(
                    Result.Success(Unit)
                )

            repository.observeLanguages(
                OWNER_NAME,
                REPO_NAME
            ).test {
                assertThat(awaitItem())
                    .containsExactlyElementsIn(
                        RepoLanguageMapper.toDomain(
                            listOf(
                                RepoLanguageEntity(
                                    repoId = REPO_ID,
                                    language = "Kotlin",
                                    bytes = 300L
                                ),
                                RepoLanguageEntity(
                                    repoId = REPO_ID,
                                    language = "Java",
                                    bytes = 100L
                                )
                            )
                        )
                    )
                    .inOrder()

                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun refreshReleases_success_persists_and_observe_emits_releases() =
        runTest {
            // Arrange
            insertRepoSearchEntity()
            insertRepoEntity()

            val repository =
                repository(
                    StandardTestDispatcher(
                        testScheduler
                    )
                )

            val releases =
                listOf(
                    releaseDto(
                        id = 1L,
                        name = "First release",
                        tagName = "v1.0.0",
                        body = "Initial stable release",
                        publishedAt = "2024-01-01T00:00:00Z"
                    ),
                    releaseDto(
                        id = 2L,
                        name = "Second release",
                        tagName = "v2.0.0",
                        body = "Second stable release",
                        publishedAt = "2024-02-01T00:00:00Z"
                    )
                )

            coEvery {
                service.getReleases(
                    OWNER_NAME,
                    REPO_NAME
                )
            } returns releases

            // Act
            val result =
                repository.refreshReleases(
                    OWNER_NAME,
                    REPO_NAME
                )

            // Assert
            assertThat(result)
                .isEqualTo(
                    Result.Success(Unit)
                )

            val expectedReleaseEntities: List<RepoReleaseEntity> =
                RepoReleaseMapper.fromDomain(
                    repoId = REPO_ID,
                    releases = ReleaseMapper.fromReleaseDtoList(
                        releases
                    )
                ).sortedByDescending {
                    it.date
                }

            val expectedReleases =
                RepoReleaseMapper.toDomain(
                    expectedReleaseEntities
                )

            repository.observeReleases(
                OWNER_NAME,
                REPO_NAME
            ).test {
                assertThat(awaitItem())
                    .containsExactlyElementsIn(
                        expectedReleases
                    )
                    .inOrder()

                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun refreshLanguages_returns_failure_when_repo_missing() =
        runTest {
            // Arrange
            val repository =
                repository(
                    StandardTestDispatcher(
                        testScheduler
                    )
                )

            // Act
            val result =
                repository.refreshLanguages(
                    OWNER_NAME,
                    REPO_NAME
                )

            // Assert
            assertThat(result)
                .isEqualTo(
                    Result.Failure(DomainError.Unknown)
                )

            repository.observeLanguages(
                OWNER_NAME,
                REPO_NAME
            ).test {
                assertThat(awaitItem())
                    .isEmpty()

                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun refreshReleases_returns_failure_when_repo_missing() =
        runTest {
            // Arrange
            val repository =
                repository(
                    StandardTestDispatcher(
                        testScheduler
                    )
                )

            // Act
            val result =
                repository.refreshReleases(
                    OWNER_NAME,
                    REPO_NAME
                )

            // Assert
            assertThat(result)
                .isEqualTo(
                    Result.Failure(DomainError.Unknown)
                )

            repository.observeReleases(
                OWNER_NAME,
                REPO_NAME
            ).test {
                assertThat(awaitItem())
                    .isEmpty()

                cancelAndIgnoreRemainingEvents()
            }
        }

    private fun repository(
        ioDispatcher: CoroutineDispatcher
    ): RepoDetailRepositoryImpl {
        return RepoDetailRepositoryImpl(
            service = service,
            repoDao = repoDao,
            repoLanguageDao = repoLanguageDao,
            repoReleaseDao = repoReleaseDao,
            errorParser = errorParser,
            database = database,
            ioDispatcher = ioDispatcher
        )
    }

    private suspend fun insertRepoSearchEntity(
        username: String = OWNER_NAME
    ) {
        repoDao.upsertSearchUser(
            RepoSearchEntity(
                username = username,
                ownerId = OWNER_ID,
                avatarUrl = "avatar-url",
                profileUrl = "profile-url",
                lastSync = 0L,
                lastAccessed = 0L
            )
        )
    }

    private suspend fun insertRepoEntity(
        repo: RepoEntity = repoEntity()
    ) {
        repoDao.upsertRepos(
            listOf(repo)
        )
    }

    private fun repoEntity(
        id: Long = REPO_ID,
        name: String = REPO_NAME,
        username: String = OWNER_NAME
    ): RepoEntity {
        return RepoEntity(
            id = id,
            name = name,
            description = "Sample Description",
            language = "Kotlin",
            stars = 100,
            forks = 25,
            topics = listOf(
                "android",
                "compose"
            ),
            username = username
        )
    }

    private fun releaseDto(
        id: Long,
        name: String,
        tagName: String,
        body: String,
        publishedAt: String
    ): ReleaseDto {
        return ReleaseDto(
            id = id,
            name = name,
            tag_name = tagName,
            body = body,
            published_at = publishedAt,
            html_url = "https://github.com/$OWNER_NAME/$REPO_NAME/releases/tag/$tagName"
        )
    }

    private companion object {
        const val OWNER_NAME = "karthik-pro-engr"
        const val OWNER_ID = 100L
        const val REPO_ID = 1L
        const val REPO_NAME = "compose-playground"
    }
}
