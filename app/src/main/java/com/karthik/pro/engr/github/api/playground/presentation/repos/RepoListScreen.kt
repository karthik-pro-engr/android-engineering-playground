package com.karthik.pro.engr.github.api.playground.presentation.repos

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.karthik.pro.engr.github.api.domain.model.Repo
import com.karthik.pro.engr.github.api.playground.R
import com.karthik.pro.engr.github.api.playground.presentation.components.OfflineBanner
import com.karthik.pro.engr.github.api.playground.presentation.handlers.PagingScreenHandler
import com.karthik.pro.engr.github.api.playground.presentation.repos.GithubRepoListTestTags.SEARCH_BUTTON
import com.karthik.pro.engr.github.api.playground.presentation.repos.GithubRepoListTestTags.SEARCH_INPUT
import kotlinx.coroutines.flow.Flow

@Composable
fun RepoListScreen(
    modifier: Modifier = Modifier,
    currentQuery: String?,
    reposSharedFlow: Flow<PagingData<Repo>>,
    onRepoClick: (String, String) -> Unit,
    onSubmit: (String) -> Unit,
) {

    Column(
        modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = stringResource(
                R.string.github_repo_explorer
            ),

            style =
                MaterialTheme.typography.headlineSmall
        )

        Text(
            text = stringResource(
                R.string.enter_github_username
            ),

            style =
                MaterialTheme.typography.bodyMedium,

            color =
                MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(Modifier.height(24.dp))

        ReposSearchByUserName(
            currentQuery = currentQuery,
            onSubmit = onSubmit
        )

        Spacer(Modifier.height(16.dp))

        if (currentQuery.isNullOrBlank()) {
            Box(
                modifier = modifier
                    .fillMaxSize()
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center),
                    text = stringResource(R.string.repos_info),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        } else {

            val lazyPagingItems = reposSharedFlow.collectAsLazyPagingItems()

            val loadState = lazyPagingItems.loadState

            if (
                lazyPagingItems.itemCount > 0 &&
                loadState.refresh is LoadState.Error
            ) {
                OfflineBanner()
            }

            PagingScreenHandler(lazyPagingItems = lazyPagingItems, emptyContent = {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(stringResource(R.string.no_repos_found))
                }
            }) { repo ->
                RepoListItem(
                    repo = repo,
                    onRepoClick = onRepoClick
                )
            }

        }

    }
}

@Composable
fun ReposSearchByUserName(
    modifier: Modifier = Modifier,
    currentQuery: String?,
    onSubmit: (String) -> Unit
) {
    var username by remember { mutableStateOf(currentQuery ?: "") }

    Row(
        modifier = modifier.padding(5.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            modifier = Modifier
                .testTag(SEARCH_INPUT)
                .weight(1f)
                .height(IntrinsicSize.Min),
            label = { Text(stringResource(R.string.repos_label_username)) }
        )

        Spacer(Modifier.width(8.dp))

        Button(modifier = Modifier.testTag(SEARCH_BUTTON), onClick = { onSubmit(username) }) {
            Text(stringResource(R.string.fetch))
        }
    }
}

