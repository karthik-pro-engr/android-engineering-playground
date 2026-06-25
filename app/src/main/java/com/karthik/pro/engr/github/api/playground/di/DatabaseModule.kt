package com.karthik.pro.engr.github.api.playground.di

import android.content.Context
import androidx.room.Room
import com.karthik.pro.engr.github.api.data.local.dao.RemoteKeysDao
import com.karthik.pro.engr.github.api.data.local.dao.RepoDao
import com.karthik.pro.engr.github.api.data.local.dao.RepoLanguageDao
import com.karthik.pro.engr.github.api.data.local.dao.RepoReleaseDao
import com.karthik.pro.engr.github.api.data.local.database.DatabaseConstants.DATABASE_NAME
import com.karthik.pro.engr.github.api.data.local.database.DatabaseMigrations
import com.karthik.pro.engr.github.api.data.local.database.GithubDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): GithubDatabase {
        return Room.databaseBuilder(
            context,
            GithubDatabase::class.java,
            DATABASE_NAME
        )
            .addMigrations(
                DatabaseMigrations.MIGRATION_1_2
            )
            .build()
    }

    @Provides
    @Singleton
    fun provideRepoDao(
        database: GithubDatabase
    ): RepoDao {
        return database.repoDao()
    }

    @Provides
    @Singleton
    fun provideRepoLanguagesDao(
        database: GithubDatabase
    ): RepoLanguageDao {
        return database.repoLanguageDao()
    }

    @Provides
    @Singleton
    fun provideRepoReleaseDao(
        database: GithubDatabase
    ): RepoReleaseDao {
        return database.repoReleaseDao()
    }

    @Provides
    @Singleton
    fun provideRemoteKeysDao(
        database: GithubDatabase
    ): RemoteKeysDao {
        return database.remoteKeysDao()
    }


}