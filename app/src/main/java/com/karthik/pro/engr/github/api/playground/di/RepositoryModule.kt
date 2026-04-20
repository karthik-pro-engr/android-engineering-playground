package com.karthik.pro.engr.github.api.playground.di

import com.karthik.pro.engr.github.api.data.remote.repository.GithubRepositoryImpl
import com.karthik.pro.engr.github.api.data.repository.FakeRepository
import com.karthik.pro.engr.github.api.domain.repository.GithubRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindsGithubRepository(githubRepositoryImpl: FakeRepository): GithubRepository


}