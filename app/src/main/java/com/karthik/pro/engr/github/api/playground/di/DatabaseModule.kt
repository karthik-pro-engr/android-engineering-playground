package com.karthik.pro.engr.github.api.playground.di

import android.content.Context
import androidx.room.Room
import com.karthik.pro.engr.github.api.data.local.dao.RemoteKeysDao
import com.karthik.pro.engr.github.api.data.local.dao.RepoDao
import com.karthik.pro.engr.github.api.data.local.database.DatabaseConstants.DATABASE_NAME
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
    fun provideGithubDatabase(
        @ApplicationContext context: Context
    ): GithubDatabase {
        return Room.databaseBuilder(
            context,
            GithubDatabase::class.java,
            DATABASE_NAME
        ).build()
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
    fun provideRemoteKeysDao(
        database: GithubDatabase
    ): RemoteKeysDao {
        return database.remoteKeysDao()
    }


}