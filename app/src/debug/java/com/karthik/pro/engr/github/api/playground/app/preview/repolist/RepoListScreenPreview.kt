package com.karthik.pro.engr.github.api.playground.app.preview.repolist

import androidx.compose.runtime.Composable
import androidx.paging.PagingData
import com.karthik.pro.engr.devtools.AllVariantsPreview
import com.karthik.pro.engr.github.api.core.testing.RepoFactory
import com.karthik.pro.engr.github.api.playground.presentation.repos.RepoListScreen
import kotlinx.coroutines.flow.flowOf
import kotlin.random.Random


@AllVariantsPreview
@Composable
private fun RepoListScreenPreview() {
    RepoListScreen(
        currentQuery = "karthik-pro-engr",
        reposSharedFlow = flowOf(
            PagingData.from(
                List(10) { RepoFactory.withId(Random.nextLong()) }
            )
        ),
        onRepoClick = { _, _ -> },
        onSubmit = {}
    )
}