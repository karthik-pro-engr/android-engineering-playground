package com.karthik.pro.engr.github.api.playground.presentation.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.karthik.pro.engr.github.api.playground.R
import com.karthik.pro.engr.github.api.playground.presentation.designsystem.Dimens

@Composable
fun OfflineBanner(
    modifier: Modifier = Modifier,
    @StringRes resId: Int
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.errorContainer
    ) {
        Text(
            modifier = Modifier.padding(Dimens.medium),
            text = stringResource(
                resId
            ),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onErrorContainer
        )
    }
}