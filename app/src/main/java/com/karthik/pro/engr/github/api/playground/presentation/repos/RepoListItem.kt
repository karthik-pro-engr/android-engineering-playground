package com.karthik.pro.engr.github.api.playground.presentation.repos

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.karthik.pro.engr.github.api.domain.model.Repo
import com.karthik.pro.engr.github.api.playground.presentation.repos.GithubRepoListTestTags.REPO_ITEM

@Composable
fun RepoListItem(
    modifier: Modifier = Modifier, repo: Repo,
    onRepoClick: (String, String) -> Unit
) {

    Column(
        modifier
            .testTag(REPO_ITEM)
            .fillMaxWidth()
            .padding(top = 8.dp, start = 8.dp, end = 8.dp)
            .clickable {
                onRepoClick(repo.ownerName, repo.name)
            }
    ) {
        Text(
            repo.name,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(5.dp)
        )
        repo.description?.let {
            Text(
                text = it, style = MaterialTheme.typography.bodyMedium,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                "⭐ ${repo.stars}",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(5.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                repo.language ?: "-",
                style = MaterialTheme.typography.bodySmall, modifier = modifier.padding(5.dp)
            )
        }
        HorizontalDivider()
    }

}
