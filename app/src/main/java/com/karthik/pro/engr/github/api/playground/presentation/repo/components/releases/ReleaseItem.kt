package com.karthik.pro.engr.github.api.playground.presentation.repo.components.releases

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.karthik.pro.engr.devtools.AllVariantsPreview
import com.karthik.pro.engr.github.api.playground.R
import com.karthik.pro.engr.github.api.playground.presentation.components.Badge
import com.karthik.pro.engr.github.api.playground.presentation.repo.RepoDetailTestTags.RELEASE_ITEM
import com.karthik.pro.engr.github.api.playground.presentation.repo.components.releases.model.ReleaseUi

@Composable
fun ReleaseItem(
    release: ReleaseUi,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .testTag(RELEASE_ITEM)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(12.dp)) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        modifier = Modifier
                            .weight(1f)
                            .padding(5.dp),
                        text = release.version,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Badge(
                        text = stringResource(R.string.latest),
                        backgroundColor = MaterialTheme.colorScheme.primary.copy(0.15f),
                        textColor = MaterialTheme.colorScheme.primary
                    )

                }
                Text(release.date, style = MaterialTheme.typography.bodySmall)
            }
            IconButton(onClick = onClick) {
                Icon(
                    imageVector = Icons.Filled.ChevronRight,
                    contentDescription = null
                )
            }
        }
    }
}

@AllVariantsPreview
@Composable
private fun ReleaseItemPreview() {
    ReleaseItem(
        ReleaseUi(12345, "v1.0.000000000000000000000000000000000000", "Yesterday", true)
    ) { }
}