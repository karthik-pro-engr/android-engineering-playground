package com.karthik.pro.engr.github.api.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.karthik.pro.engr.github.api.data.local.converter.ListStringConverter
import com.karthik.pro.engr.github.api.data.local.dao.RemoteKeysDao
import com.karthik.pro.engr.github.api.data.local.dao.RepoDao
import com.karthik.pro.engr.github.api.data.local.dao.RepoLanguageDao
import com.karthik.pro.engr.github.api.data.local.dao.RepoReleaseDao
import com.karthik.pro.engr.github.api.data.local.entity.RemoteKeysEntity
import com.karthik.pro.engr.github.api.data.local.entity.RepoEntity
import com.karthik.pro.engr.github.api.data.local.entity.RepoLanguageEntity
import com.karthik.pro.engr.github.api.data.local.entity.RepoReleaseEntity
import com.karthik.pro.engr.github.api.data.local.entity.RepoSearchEntity

@Database(
    entities = [
        RepoSearchEntity::class,
        RepoEntity::class,
        RemoteKeysEntity::class,
        RepoLanguageEntity::class,
        RepoReleaseEntity::class
    ],
    version = 2,
    exportSchema = true
)
@TypeConverters(
    ListStringConverter::class
)
abstract class GithubDatabase : RoomDatabase() {

    abstract fun repoDao(): RepoDao

    abstract fun remoteKeysDao(): RemoteKeysDao

    abstract fun repoLanguageDao(): RepoLanguageDao

    abstract fun repoReleaseDao(): RepoReleaseDao
}