package com.karthik.pro.engr.github.api.playground.presentation.repos

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@Composable
fun RepoListRoute(
    modifier: Modifier = Modifier,
    viewModel: GithubReposListViewModel = hiltViewModel(),
    onRepoClick: (String, String) -> Unit
) {
    val currentQuery by viewModel.currentQuery.collectAsState()
    RepoListScreen(
        modifier = modifier,
        currentQuery = currentQuery,
        reposSharedFlow = viewModel.reposSharedFlow,
        onRepoClick = onRepoClick,
        onSubmit = viewModel::submitQuery
    )
}