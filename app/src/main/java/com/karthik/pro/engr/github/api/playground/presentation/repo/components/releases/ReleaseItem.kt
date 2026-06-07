package com.karthik.pro.engr.github.api.playground.presentation.repo.components.releases

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.karthik.pro.engr.github.api.domain.time.RelativeTime
import com.karthik.pro.engr.github.api.playground.R
import com.karthik.pro.engr.github.api.playground.presentation.components.Badge
import com.karthik.pro.engr.github.api.playground.presentation.designsystem.Dimens
import com.karthik.pro.engr.github.api.playground.presentation.repo.RepoDetailTestTags.RELEASE_ITEM
import com.karthik.pro.engr.github.api.playground.presentation.repo.components.releases.model.ReleaseUi
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun ReleaseItem(
    release: ReleaseUi,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Dimens.small)
            .testTag(RELEASE_ITEM),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(Dimens.large),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        modifier = Modifier
                            .weight(1f),
                        text = release.version,
                        style = MaterialTheme.typography.titleMedium
                    )
                    if (release.isLatest) {
                        Badge(
                            text = stringResource(R.string.latest),
                            backgroundColor = MaterialTheme.colorScheme.primary.copy(0.15f),
                            textColor = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                Spacer(modifier = Modifier.height(Dimens.xs))
                Text(release.date.toUiString(), style = MaterialTheme.typography.bodySmall)
            }
            IconButton(onClick = onClick) {
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null
                )
            }
        }
    }
}


@Composable
fun RelativeTime.toUiString(): String {
    return when (this) {
        RelativeTime.JustNow -> stringResource(R.string.just_now)

        is RelativeTime.MinutesAgo ->
            pluralStringResource(R.plurals.minutes_ago, value.toInt(), value)

        is RelativeTime.HoursAgo -> pluralStringResource(R.plurals.hours_ago, value.toInt(), value)

        is RelativeTime.DaysAgo -> stringResource(R.string.days_ago, value)

        is RelativeTime.Yesterday -> stringResource(R.string.yesterday)

        is RelativeTime.Date -> {
            val formatter = DateTimeFormatter
                .ofPattern("MMM d, yyyy")
                .withZone(ZoneId.systemDefault())

            formatter.format(value)
        }

    }
}
