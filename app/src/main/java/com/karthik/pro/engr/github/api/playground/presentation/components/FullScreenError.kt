package com.karthik.pro.engr.github.api.playground.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.karthik.pro.engr.github.api.playground.presentation.repos.GithubRepoListTestTags.FULL_SCREEN_ERROR

@Composable
fun FullScreenError(
    error: String,
    onRetry: () -> Unit
) {
    Column(
        Modifier.fillMaxSize()
            .testTag(FULL_SCREEN_ERROR),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ErrorUi(error = error,
            onClick = onRetry)
    }
}