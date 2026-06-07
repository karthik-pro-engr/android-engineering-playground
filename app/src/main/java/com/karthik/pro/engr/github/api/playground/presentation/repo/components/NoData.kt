package com.karthik.pro.engr.github.api.playground.presentation.repo.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.karthik.pro.engr.github.api.playground.presentation.repo.RepoDetailTestTags.DESCRIPTION

@Composable
fun NoData(
    modifier: Modifier = Modifier,
    @StringRes value: Int
) {
    Box(modifier = modifier) {
        Text(
            modifier = Modifier
                .testTag(DESCRIPTION)
                .padding(5.dp),
            text = stringResource(value), style = MaterialTheme.typography.bodyMedium
        )
    }
}