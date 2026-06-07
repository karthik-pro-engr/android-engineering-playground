package com.karthik.pro.engr.github.api.playground.presentation.repo.components.topics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
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
        modifier = Modifier.fillMaxWidth().testTag(RepoDetailTestTags.TOPICS),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        topics.forEach {
            AssistChip(onClick = {}, label = { Text(it) })
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