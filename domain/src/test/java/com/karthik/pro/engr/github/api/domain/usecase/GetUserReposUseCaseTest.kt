package com.karthik.pro.engr.github.api.domain.usecase

import androidx.paging.PagingData
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.karthik.pro.engr.github.api.domain.model.Owner
import com.karthik.pro.engr.github.api.domain.model.Repo
import com.karthik.pro.engr.github.api.domain.repository.GithubRepository
import com.karthik.pro.engr.github.api.domain.result.Result
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetUserReposUseCaseTest {
    private val repo = mockk<GithubRepository>()
    private val useCase = GetUserReposUseCase(repo)

    @Test
    fun `invoke return list of repos`() = runTest {
        val expected = PagingData.from(
            listOf(
                Repo(
                    id = 1069192744,
                    name = "admin-tools",
                    fullName = "karthik-pro-engr/admin-tools",
                    description = "",
                    htmlUrl = "https://github.com/karthik-pro-engr",
                    language = "Shell",
                    stars = 0,
                    forks = 0,
                    owner = Owner(
                        login = "karthik-pro-engr",
                        id = 101930095,
                        avatarUrl = "https://avatars.githubusercontent.com/u/101930095?v=4",
                        htmlUrl = "https://github.com/karthik-pro-engr"
                    )
                )
            )
        )
        coEvery {
            repo.getUserRepos("karthik-pro-engr")
        } returns kotlinx.coroutines.flow.flow {
            emit(expected)
        }

        useCase.invoke("karthik-pro-engr").test {
            val item = awaitItem()
            assertThat(item).isEqualTo(expected)
            cancelAndIgnoreRemainingEvents()
        }
    }
}