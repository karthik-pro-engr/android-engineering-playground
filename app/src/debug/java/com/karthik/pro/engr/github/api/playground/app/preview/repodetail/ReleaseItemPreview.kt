package com.karthik.pro.engr.github.api.playground.app.preview.repodetail

import androidx.compose.runtime.Composable
import com.karthik.pro.engr.devtools.AllVariantsPreview
import com.karthik.pro.engr.github.api.core.testing.RepoFactory
import com.karthik.pro.engr.github.api.playground.presentation.common.formatter.RelativeTimeFormatter
import com.karthik.pro.engr.github.api.playground.presentation.repo.components.releases.ReleaseItem
import com.karthik.pro.engr.github.api.playground.presentation.repo.mapper.toReleaseUi

@AllVariantsPreview
@Composable
private fun ReleaseItemPreview() {
    ReleaseItem(
        release = RepoFactory.defaultReleaseItem().toReleaseUi(RelativeTimeFormatter()),
        onClick = {}
    )
}