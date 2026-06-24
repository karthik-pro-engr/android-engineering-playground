package com.karthik.pro.engr.github.api.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.karthik.pro.engr.github.api.data.local.entity.RepoLanguageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RepoLanguageDao {

    @Query(
        """
        SELECT *
        FROM RepoLanguageEntity
        WHERE repoId = :repoId
        ORDER BY bytes DESC
        """
    )
    fun observeLanguages(
        repoId: Long
    ): Flow<List<RepoLanguageEntity>>

    @Upsert
    suspend fun upsertLanguages(
        languages: List<RepoLanguageEntity>
    )

    @Query(
        """
        DELETE FROM RepoLanguageEntity
        WHERE repoId = :repoId
        """
    )
    suspend fun deleteLanguages(
        repoId: Long
    )
}