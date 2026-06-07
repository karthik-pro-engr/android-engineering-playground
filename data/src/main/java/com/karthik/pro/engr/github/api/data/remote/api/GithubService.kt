package com.karthik.pro.engr.github.api.data.remote.api

import com.karthik.pro.engr.github.api.data.remote.dto.response.GitHubRepoDto
import com.karthik.pro.engr.github.api.data.remote.dto.response.ReleaseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubService {
    @GET("users/{username}/repos")
    suspend fun listUserRepos(
        @Path("username") username: String,
        @Query("per_page") perPage: Int = 30,
        @Query("page") page: Int = 1
    ): Response<List<GitHubRepoDto>>

    @GET("repos/{owner}/{repo}")
    suspend fun repoDetail(
        @Path("owner") owner: String,
        @Path("repo") repoName: String
    ): GitHubRepoDto

    @GET("repos/{owner}/{repo}/languages")
    suspend fun getRepoLanguages(
        @Path("owner") owner: String,
        @Path("repo") repoName: String
    ): Map<String, Long>

    @GET("repos/{owner}/{repo}/releases")
    suspend fun getReleases(
        @Path("owner") owner: String,
        @Path("repo") repo: String
    ): List<ReleaseDto>
}