package com.karthik.pro.engr.github.api.playground.di

import com.karthik.pro.engr.github.api.data.remote.graphql.datasource.GithubGraphqlDataSource
import com.karthik.pro.engr.github.api.data.remote.graphql.datasource.GithubGraphqlDataSourceImpl
import com.karthik.pro.engr.github.api.data.remote.repository.GithubRepositoryImpl
import com.karthik.pro.engr.github.api.data.remote.repository.RepoDetailRepositoryImpl
import com.karthik.pro.engr.github.api.domain.repository.GithubRepository
import com.karthik.pro.engr.github.api.domain.repository.RepoDetailRepository
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
    abstract fun bindsGithubRepository(githubRepositoryImpl: GithubRepositoryImpl): GithubRepository


    @Binds
    abstract fun bindRepoDetailRepository(
        impl: RepoDetailRepositoryImpl
    ): RepoDetailRepository

    @Binds
    abstract fun bindGraphqlDataSource(dataSourceImpl: GithubGraphqlDataSourceImpl):GithubGraphqlDataSource

}