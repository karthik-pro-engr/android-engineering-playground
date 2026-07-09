package com.karthik.pro.engr.github.api.data.remote.di

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.network.okHttpClient
import com.karthik.pro.engr.github.api.data.BuildConfig
import com.karthik.pro.engr.github.api.data.remote.api.GithubService
import com.karthik.pro.engr.github.api.data.remote.error.ErrorParser
import com.karthik.pro.engr.github.api.data.remote.graphql.GithubGraphqlAuthInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val BASE_URL = "https://api.github.com/"
    private const val GRAPHQL_URL =
        "https://api.github.com/graphql"
    @Provides
    @Singleton
    fun provideLogging(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return httpLoggingInterceptor
    }


    @Provides
    @Singleton
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()


    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    @Provides
    @Singleton
    fun provideGithubService(retrofit: Retrofit): GithubService =
        retrofit.create(GithubService::class.java)

    @Provides
    @Singleton
    fun provideErrorDto(retrofit: Retrofit): ErrorParser = ErrorParser(retrofit)

    @Provides
    @Singleton
    @Named("graphql")
    fun provideGraphqlOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
    ): OkHttpClient {


        val token = BuildConfig.GRAPHQL_TOKEN
            ?: error(
                "GRAPHQL_TOKEN environment variable not found"
            )

        return OkHttpClient.Builder()
            .addInterceptor(
                GithubGraphqlAuthInterceptor(token)
            )
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideApolloClient(
        @Named("graphql")
        okHttpClient: OkHttpClient
    ): ApolloClient {

        return ApolloClient.Builder()
            .serverUrl(GRAPHQL_URL)
            .okHttpClient(okHttpClient)
            .build()
    }

}