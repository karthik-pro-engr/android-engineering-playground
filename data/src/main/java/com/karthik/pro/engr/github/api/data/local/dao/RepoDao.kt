package com.karthik.pro.engr.github.api.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.karthik.pro.engr.github.api.data.local.entity.RepoEntity


@Dao
interface RepoDao {
    @Query("""
        SELECT *
        FROM RepoEntity
        WHERE username = :username
    """)
    fun pagingSource(username: String): PagingSource<Int, RepoEntity>

    @Upsert
    suspend fun upsertRepos(
        repos: List<RepoEntity>
    )

    @Query("""
        SELECT id
        FROM RepoEntity
        WHERE username = :username
    """)
    suspend fun getRepoIdsByUsername(
        username: String
    ): List<Long>

    @Query("""
        DELETE FROM RepoEntity
        WHERE id IN (:repoIds)
    """)
    suspend fun deleteReposByIds(
        repoIds: List<Long>
    )

    @Query("""
        DELETE FROM RepoEntity
        WHERE username = :username
    """)
    suspend fun deleteReposByUsername(
        username: String
    )
}