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
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.karthik.pro.engr.devtools.AllVariantsPreview
import com.karthik.pro.engr.github.api.domain.model.Owner
import com.karthik.pro.engr.github.api.domain.model.Repo
import com.karthik.pro.engr.github.api.playground.R
import com.karthik.pro.engr.github.api.playground.presentation.handlers.PagingScreenHandler
import com.karthik.pro.engr.github.api.playground.presentation.repos.GithubRepoListTestTags.SEARCH_BUTTON
import com.karthik.pro.engr.github.api.playground.presentation.repos.GithubRepoListTestTags.SEARCH_INPUT
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

@Composable
fun RepoListScreen(
    modifier: Modifier = Modifier,
    currentQuery: String?,
    reposSharedFlow: Flow<PagingData<Repo>>,
    onSubmit: (String) -> Unit,
) {

    Column(
        modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

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

            PagingScreenHandler(lazyPagingItems = lazyPagingItems, emptyContent = {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(stringResource(R.string.no_repos_found))
                }
            }) { repo ->
                RepoListItem(repo = repo)
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


@AllVariantsPreview
@Composable
private fun RepoListScreenPreview() {
    val reposSharedFlow = flowOf(
        PagingData.from(
            listOf(
                Repo(
                    id = 1,
                    name = "admin-tools",
                    fullName = "karthik-pro-engr/admin-tools",
                    description = "Automates applying branch rulesets to new repositories.",
                    htmlUrl = "https://github.com/karthik-pro-engr/admin-tools",
                    language = "Shell",
                    stars = 5,
                    forks = 1,
                    owner = Owner(
                        login = "karthik-pro-engr",
                        id = 101930095,
                        avatarUrl = "https://avatars.githubusercontent.com/u/101930095?v=",
                        htmlUrl = "https://github.com/karthik-pro-engr"
                    )
                ), Repo(
                    id = 2,
                    name = "admin-tools",
                    fullName = "karthik-pro-engr/admin-tools",
                    description = "Automates applying branch rulesets to new repositories.",
                    htmlUrl = "https://github.com/karthik-pro-engr/admin-tools",
                    language = "Shell",
                    stars = 5,
                    forks = 1,
                    owner = Owner(
                        login = "karthik-pro-engr",
                        id = 101930095,
                        avatarUrl = "https://avatars.githubusercontent.com/u/101930095?v=",
                        htmlUrl = "https://github.com/karthik-pro-engr"
                    )
                )
            )
        )
    )
    RepoListScreen(
        currentQuery = "",
        reposSharedFlow = reposSharedFlow,
        onSubmit = { println("clicked") }
    )
}