package com.karthik.pro.engr.github.api.playground.presentation.repo

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@Composable
fun RepoDetailRoute(
    modifier: Modifier = Modifier,
    viewModel: RepoDetailViewModel = hiltViewModel()
) {
    val items by viewModel.uiItems.collectAsState()

    RepoDetailScreen(
        modifier = modifier,
        items = items, onRepoRetry = viewModel::retryRepoDetail,
        onLanguageRetry = viewModel::retryLanguages,
        onReleaseRetry = viewModel::retryReleases
    )

}