package com.karthik.pro.engr.github.api.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.karthik.pro.engr.github.api.data.local.entity.RepoReleaseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RepoReleaseDao {

    @Query(
        """
        SELECT *
        FROM RepoReleaseEntity
        WHERE repoId = :repoId
        ORDER BY date DESC
        """
    )
    fun observeReleases(
        repoId: Long
    ): Flow<List<RepoReleaseEntity>>

    @Upsert
    suspend fun upsertReleases(
        releases: List<RepoReleaseEntity>
    )

    @Query(
        """
        DELETE FROM RepoReleaseEntity
        WHERE repoId = :repoId
        """
    )
    suspend fun deleteReleases(
        repoId: Long
    )
}