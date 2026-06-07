package com.karthik.pro.engr.github.api.playground.presentation.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

sealed class UiText {
    data class Dynamic(val value: String) : UiText()
    data class StringRes(@param:androidx.annotation.StringRes val resId: Int) : UiText()
}

@Composable
fun UiText.asString(): String {
    return when (this) {
        is UiText.Dynamic -> value
        is UiText.StringRes -> stringResource(resId)
    }
}