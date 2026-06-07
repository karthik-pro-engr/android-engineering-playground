package com.karthik.pro.engr.github.api.playground.presentation.repo.components.header

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.karthik.pro.engr.devtools.AllVariantsPreview
import com.karthik.pro.engr.github.api.playground.presentation.repo.RepoDetailTestTags.DESCRIPTION
import com.karthik.pro.engr.github.api.playground.presentation.repo.RepoDetailTestTags.HEADER
import com.karthik.pro.engr.github.api.playground.presentation.repo.RepoDetailTestTags.OWNER
import com.karthik.pro.engr.github.api.playground.presentation.repo.RepoDetailTestTags.TITLE
import com.karthik.pro.engr.github.api.playground.presentation.repo.components.header.model.HeaderUi

@Composable
fun Header(
    modifier: Modifier = Modifier,
    headerUi: HeaderUi
) {
    Column(
        modifier = Modifier.fillMaxWidth().testTag(HEADER)
    ) {
        with(headerUi) {
            Text(
                modifier = Modifier
                    .testTag(OWNER)
                    .padding(5.dp),
                text = owner,
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                modifier = Modifier
                    .testTag(DESCRIPTION)
                    .padding(5.dp),
                text = description, style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@AllVariantsPreview
@Composable
private fun HeaderPreview() {
    Header(
        headerUi = HeaderUi(
            owner = "Karthik-pro-engr",
            title = "Github-Api-Playground",
            description = "Simple URL shortener for ActionBarSherlock using node.js and express."
        )
    )
}