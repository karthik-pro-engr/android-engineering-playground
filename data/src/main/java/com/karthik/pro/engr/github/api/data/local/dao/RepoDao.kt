package com.karthik.pro.engr.github.api.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.karthik.pro.engr.github.api.data.local.entity.RepoEntity
import com.karthik.pro.engr.github.api.data.local.entity.RepoSearchEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface RepoDao {
    @Query(
        """
    SELECT *
    FROM RepoSearchEntity
    WHERE username = :username
"""
    )
    suspend fun findSearchUser(
        username: String
    ): RepoSearchEntity?

    @Upsert
    suspend fun upsertSearchUser(
        searchUser: RepoSearchEntity
    )

    @Query(
        """
    DELETE FROM RepoSearchEntity
    WHERE username = :username
"""
    )
    suspend fun deleteSearchUser(
        username: String
    )

    @Query(
        """
        SELECT *
        FROM RepoEntity
        WHERE username = :username
         ORDER BY id ASC
    """
    )
    fun pagingSource(username: String): PagingSource<Int, RepoEntity>

    @Upsert
    suspend fun upsertRepos(
        repos: List<RepoEntity>
    )

    @Query(
        """
        SELECT id
        FROM RepoEntity
        WHERE username = :username
    """
    )
    suspend fun getRepoIdsByUsername(
        username: String
    ): List<Long>

    @Query(
        """
        DELETE FROM RepoEntity
        WHERE id IN (:repoIds)
    """
    )
    suspend fun deleteReposByIds(
        repoIds: List<Long>
    )

    @Query(
        """
        DELETE FROM RepoEntity
        WHERE username = :username
    """
    )
    suspend fun deleteReposByUsername(
        username: String
    )

    @Query(
        """
    UPDATE RepoSearchEntity
    SET lastAccessed = :lastAccessed
    WHERE username = :username
    """
    )
    suspend fun updateLastAccessed(
        username: String,
        lastAccessed: Long
    )

    @Query(
        """
    DELETE FROM RepoSearchEntity
    WHERE lastAccessed < :cutoffTime
    """
    )
    suspend fun deleteInactiveUser(
        cutoffTime: Long
    )

    @Query(
        """
    SELECT *
    FROM RepoEntity
    WHERE username = :ownerName
      AND name = :repoName
    LIMIT 1
    """
    )
    fun observeRepo(
        ownerName: String,
        repoName: String
    ): Flow<RepoEntity?>

    @Query(
        """
    SELECT *
    FROM RepoEntity
    WHERE username = :ownerName
      AND name = :repoName
    LIMIT 1
    """
    )
    suspend fun getRepo(
        ownerName: String,
        repoName: String
    ): RepoEntity?
}