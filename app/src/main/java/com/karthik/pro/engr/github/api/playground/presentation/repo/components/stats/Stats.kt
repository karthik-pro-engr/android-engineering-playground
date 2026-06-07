package com.karthik.pro.engr.github.api.playground.presentation.repo.components.stats

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.karthik.pro.engr.devtools.AllVariantsPreview
import com.karthik.pro.engr.github.api.playground.presentation.designsystem.Dimens
import com.karthik.pro.engr.github.api.playground.presentation.repo.RepoDetailTestTags.FORKS
import com.karthik.pro.engr.github.api.playground.presentation.repo.RepoDetailTestTags.STARS

@Composable
fun Stats(
    modifier: Modifier = Modifier,
    statsUi: StatsUi
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(
            space = Dimens.medium, alignment = Alignment.CenterHorizontally
        )
    ) {
        with(statsUi) {
            StatCard("Stars", stars, tag = STARS)
            StatCard("Forks", forks, FORKS)
        }
    }
}

@Composable
fun StatCard(
    label: String, value: String, tag: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .testTag(tag),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(Dimens.large),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge,
            )
            Text(label, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@AllVariantsPreview
@Composable
private fun StatsPreview() {
    Stats(statsUi = StatsUi(stars = "16.4k", forks = "24.5k"))
}