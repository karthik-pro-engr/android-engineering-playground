package com.karthik.pro.engr.github.api.data.util

import com.google.common.truth.Truth.assertThat
import com.karthik.pro.engr.github.api.data.remote.dto.response.GitHubOwnerDto
import com.karthik.pro.engr.github.api.data.remote.dto.response.GitHubRepoDto
import com.karthik.pro.engr.github.api.data.remote.error.NetworkError
import com.karthik.pro.engr.github.api.data.remote.util.safeApiCall
import com.karthik.pro.engr.github.api.domain.result.Result
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import okio.IOException
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response

class SafeApiCallTest {
    @Test
    fun `safeApiCall return Success when lambda returns value`() = runTest {
        val listRepoDtos = listOf(
            GitHubRepoDto(
                id = 1,
                name = "algo-compose",
                description = "A comprehensive Android practice lab for mastering DSA, Jetpack Compose, and Android architecture concepts through real-world scenario-based projects",
                language = "Kotlin",
                stargazers_count = 0,
                forks_count = 0,
                topics = emptyList(),
                owner = GitHubOwnerDto(
                    login = "karthik-pro-engr",
                    id = 101930095,
                    avatar_url = "https://avatars.githubusercontent.com/u/101930095?v=4",
                    html_url = "https://github.com/karthik-pro-engr"
                )
            )
        )

        val safeApiCallResult = safeApiCall { listRepoDtos }
        assertThat(safeApiCallResult).isInstanceOf(Result.Success::class.java)
        val data = (safeApiCallResult as Result.Success).data
        assertThat(data).isEqualTo(listRepoDtos)
    }

    @Test
    fun `safeApiCall maps HttpException to NetworkError_Http`() = runTest {
        val httpException =
            HttpException(Response.error<Any>(404, "not found".toResponseBody(null)))
        val result = safeApiCall<List<GitHubRepoDto>> { throw httpException }
        assertThat(result).isInstanceOf(Result.Failure::class.java)
        val error = (result as Result.Failure).error
        assertThat(error).isInstanceOf(NetworkError.Http::class.java)
        assertThat((error as NetworkError.Http).code).isEqualTo(404)


    }

    @Test
    fun `safeApiCall maps IOException to NetworkError_NoInternet`() = runTest {
        val ioException = IOException("timeout")
        val result = safeApiCall<List<GitHubRepoDto>> { throw ioException }
        assertThat(result).isInstanceOf(Result.Failure::class.java)
        val error = (result as Result.Failure).error
        assertThat(error).isInstanceOf(NetworkError.NoInternet::class.java)
        assertThat(error).isEqualTo(NetworkError.NoInternet)
    }
}