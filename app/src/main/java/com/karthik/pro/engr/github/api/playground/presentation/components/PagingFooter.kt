package com.karthik.pro.engr.github.api.playground.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.karthik.pro.engr.github.api.playground.presentation.error.PagingErrorMapper
import com.karthik.pro.engr.github.api.playground.presentation.repos.GithubRepoListTestTags.APPEND_ERROR
import com.karthik.pro.engr.github.api.playground.presentation.repos.GithubRepoListTestTags.APPEND_LOADER

@Composable
fun PagingFooter(modifier: Modifier = Modifier, lazyPagingItems: LazyPagingItems<*>) {
    when (val append = lazyPagingItems.loadState.append) {
        is LoadState.Error -> {
            val error = PagingErrorMapper.mapPagingError(append.error)
            Column(
                modifier = Modifier
                    .testTag(APPEND_ERROR)
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ErrorUi(
                    error = error,
                    onClick = { lazyPagingItems.retry() })
            }
        }

        LoadState.Loading -> Row(
            modifier = Modifier
                .testTag(APPEND_LOADER)
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator()
        }

        else -> Unit
    }
}