package com.karthik.pro.engr.github.api.playground.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.karthik.pro.engr.github.api.playground.presentation.common.UiText
import com.karthik.pro.engr.github.api.playground.presentation.common.asString
import com.karthik.pro.engr.github.api.playground.presentation.designsystem.Dimens

@Composable
fun SectionHeader(title: UiText) {
    Text(
        text = title.asString(),
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = Dimens.large,
                vertical = Dimens.medium
            )
    )
}