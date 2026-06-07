package com.karthik.pro.engr.github.api.playground.app.preview.repolist

import androidx.compose.runtime.Composable
import com.karthik.pro.engr.devtools.AllVariantsPreview
import com.karthik.pro.engr.github.api.core.testing.RepoFactory
import com.karthik.pro.engr.github.api.playground.presentation.repos.RepoListItem


@AllVariantsPreview
@Composable
private fun RepoListItemPreview() {
    RepoListItem(
        repo = RepoFactory.defaultRepo(),
        onRepoClick = { _, _ -> }
    )
}