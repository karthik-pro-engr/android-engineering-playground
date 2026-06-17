package com.karthik.pro.engr.github.api.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.karthik.pro.engr.github.api.data.local.converter.ListStringConverter
import com.karthik.pro.engr.github.api.data.local.dao.RemoteKeysDao
import com.karthik.pro.engr.github.api.data.local.dao.RepoDao
import com.karthik.pro.engr.github.api.data.local.entity.RemoteKeysEntity
import com.karthik.pro.engr.github.api.data.local.entity.RepoEntity
import com.karthik.pro.engr.github.api.data.local.entity.RepoSearchEntity

@Database(
    entities = [RepoSearchEntity::class,
        RepoEntity::class,
        RemoteKeysEntity::class],
    version = 1,
    exportSchema = true,
)
@TypeConverters(
    ListStringConverter::class
)
abstract class GithubDatabase : RoomDatabase() {
    abstract fun repoDao(): RepoDao

    abstract fun remoteKeysDao(): RemoteKeysDao
}