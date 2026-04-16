package com.karthik.pro.engr.github.api.playground.presentation.repo.components.topics

import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.karthik.pro.engr.devtools.AllVariantsPreview
import com.karthik.pro.engr.github.api.playground.presentation.repo.RepoDetailTestTags

@Composable
fun Topics(
    modifier: Modifier = Modifier,
    topics: List<String>
) {
    FlowRow(
        modifier = Modifier.testTag(RepoDetailTestTags.TOPICS)
    ) {
        topics.forEach {
            AssistChip(modifier = modifier.padding(5.dp), onClick = {}, label = { Text(it) })
        }
    }
}

@AllVariantsPreview
@Composable
private fun TopicsPreview() {
    Topics(
        topics = listOf(
            "android",
            "android-architecture",
            "api-integration",
            "clean-architecture",
            "coroutines",
            "hilt",
            "jetpack-compose",
            "kotlin",
            "mock-webserver",
            "okhttp",
            "retrofit2",
            "room",
            "unit-testing"
        )
    )
}