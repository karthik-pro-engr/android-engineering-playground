package com.karthik.pro.engr.github.api.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.karthik.pro.engr.github.api.data.local.entity.RemoteKeysEntity

@Dao
interface RemoteKeysDao {

    @Query("""SELECT nextPage FROM RemoteKeysEntity WHERE username= :username""")
    suspend fun getNextKey(username: String): Int?

    @Upsert
    suspend fun upsertNextKey(remoteKeysEntity: RemoteKeysEntity)

    @Query("""DELETE FROM RemoteKeysEntity WHERE username=:username""")
    suspend fun deleteRemoteKeys(username: String)

}