// app/src/debug/java/com/karthik/pro/engr/github/api/playground/app/preview/AppPreviews.kt
package com.karthik.pro.engr.github.api.playground.app.preview.repodetail

import androidx.compose.runtime.Composable
import com.karthik.pro.engr.devtools.AllVariantsPreview
import com.karthik.pro.engr.github.api.playground.app.preview.fakeItems
import com.karthik.pro.engr.github.api.playground.presentation.repo.RepoDetailScreen

@AllVariantsPreview
@Composable
private fun RepoDetailScreenPreview() {
    RepoDetailScreen(
        items = fakeItems(),
        repoName = "Github-api-playground",
        onBack = {},
        onRepoRetry = {},
        onLanguageRetry = {},
        onReleaseRetry = {},
    )
}




