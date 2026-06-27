package com.karthik.pro.engr.github.api.data.remote.repository


import androidx.room.withTransaction
import com.karthik.pro.engr.github.api.core.di.IoDispatcher
import com.karthik.pro.engr.github.api.data.local.dao.RepoDao
import com.karthik.pro.engr.github.api.data.local.dao.RepoLanguageDao
import com.karthik.pro.engr.github.api.data.local.dao.RepoReleaseDao
import com.karthik.pro.engr.github.api.data.local.database.GithubDatabase
import com.karthik.pro.engr.github.api.data.local.entity.RepoSearchEntity
import com.karthik.pro.engr.github.api.data.remote.api.GithubService
import com.karthik.pro.engr.github.api.data.remote.error.ErrorParser
import com.karthik.pro.engr.github.api.data.remote.mapper.ReleaseMapper
import com.karthik.pro.engr.github.api.data.remote.mapper.RepoDomainMapper
import com.karthik.pro.engr.github.api.data.remote.mapper.RepoEntityMapper
import com.karthik.pro.engr.github.api.data.remote.mapper.RepoLanguageMapper
import com.karthik.pro.engr.github.api.data.remote.mapper.RepoReleaseMapper
import com.karthik.pro.engr.github.api.data.remote.mapper.toDomainError
import com.karthik.pro.engr.github.api.data.remote.util.safeApiCall
import com.karthik.pro.engr.github.api.domain.error.DomainError
import com.karthik.pro.engr.github.api.domain.model.Language
import com.karthik.pro.engr.github.api.domain.model.Release
import com.karthik.pro.engr.github.api.domain.model.Repo
import com.karthik.pro.engr.github.api.domain.repository.RepoDetailRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import com.karthik.pro.engr.github.api.domain.result.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map


class RepoDetailRepositoryImpl @Inject constructor(
    private val service: GithubService,
    private val repoDao: RepoDao,
    private val repoLanguageDao: RepoLanguageDao,
    private val repoReleaseDao: RepoReleaseDao,
    private val errorParser: ErrorParser,
    private val database: GithubDatabase,
    @param:IoDispatcher
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : RepoDetailRepository {

    override fun observeRepoDetail(
        ownerName: String,
        repoName: String
    ): Flow<Repo?> {

        return repoDao.observeRepo(
            ownerName = ownerName,
            repoName = repoName
        ).map { entity ->
            entity?.let(
                RepoDomainMapper::fromEntity
            )
        }
    }

    override suspend fun refreshRepoDetail(
        ownerName: String,
        repoName: String
    ): Result<Unit, DomainError> {

        return withContext(ioDispatcher) {

            when (
                val result = safeApiCall {
                    service.repoDetail(
                        ownerName,
                        repoName
                    )
                }
            ) {

                is Result.Success -> {

                    database.withTransaction {

                        repoDao.upsertSearchUser(
                            RepoSearchEntity(
                                username = ownerName,
                                lastSync = System.currentTimeMillis(),
                                lastAccessed = System.currentTimeMillis(),
                                ownerId = result.data.owner.id,
                                avatarUrl = result.data.owner.avatar_url,
                                profileUrl = result.data.owner.html_url
                            )
                        )

                        repoDao.upsertRepos(
                            listOf(
                                RepoEntityMapper.fromDto(
                                    dto = result.data,
                                    username = ownerName
                                )
                            )
                        )
                    }

                    Result.Success(Unit)
                }

                is Result.Failure -> {

                    Result.Failure(
                        result.error.toDomainError()
                    )
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun observeLanguages(
        ownerName: String,
        repoName: String
    ): Flow<List<Language>> {

        return repoDao.observeRepo(
            ownerName,
            repoName
        ).flatMapLatest { repo ->

            if (repo == null) {

                flowOf(emptyList())

            } else {

                repoLanguageDao.observeLanguages(
                    repo.id
                ).map(
                    RepoLanguageMapper::toDomain
                )
            }
        }
    }

    override suspend fun refreshLanguages(
        ownerName: String,
        repoName: String
    ): Result<Unit, DomainError> {

        return withContext(ioDispatcher) {

            val repo = repoDao.getRepo(
                ownerName,
                repoName
            ) ?: return@withContext Result.Failure(
                DomainError.Unknown
            )

            when (
                val result = safeApiCall {
                    service.getRepoLanguages(
                        ownerName,
                        repoName
                    )
                }
            ) {

                is Result.Success -> {

                    val languageEntities =
                        RepoLanguageMapper.fromApi(
                            repoId = repo.id,
                            languages = result.data
                        )
                    database.withTransaction {
                        repoLanguageDao.deleteLanguages(
                            repo.id
                        )

                        repoLanguageDao.upsertLanguages(
                            languageEntities
                        )
                    }


                    Result.Success(Unit)
                }

                is Result.Failure -> {

                    Result.Failure(
                        result.error.toDomainError()
                    )
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun observeReleases(
        ownerName: String,
        repoName: String
    ): Flow<List<Release>> {

        return repoDao.observeRepo(
            ownerName,
            repoName
        ).flatMapLatest { repo ->

            if (repo == null) {

                flowOf(emptyList())

            } else {

                repoReleaseDao.observeReleases(
                    repo.id
                ).map(
                    RepoReleaseMapper::toDomain
                )
            }
        }
    }

    override suspend fun refreshReleases(
        ownerName: String,
        repoName: String
    ): Result<Unit, DomainError> {

        return withContext(ioDispatcher) {

            val repo = repoDao.getRepo(
                ownerName,
                repoName
            ) ?: return@withContext Result.Failure(
                DomainError.Unknown
            )

            when (
                val result = safeApiCall {
                    service.getReleases(
                        ownerName,
                        repoName
                    )
                }
            ) {

                is Result.Success -> {

                    database.withTransaction {
                        repoReleaseDao.deleteReleases(
                            repo.id
                        )

                        repoReleaseDao.upsertReleases(
                            RepoReleaseMapper.fromDomain(
                                repo.id,
                                ReleaseMapper.fromReleaseDtoList(
                                    result.data
                                )
                            )
                        )
                    }



                    Result.Success(Unit)
                }

                is Result.Failure -> {

                    Result.Failure(
                        result.error.toDomainError()
                    )
                }
            }
        }
    }
}