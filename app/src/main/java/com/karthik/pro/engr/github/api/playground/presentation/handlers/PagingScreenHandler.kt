package com.karthik.pro.engr.github.api.playground.presentation.handlers

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.karthik.pro.engr.github.api.playground.presentation.components.FullScreenError
import com.karthik.pro.engr.github.api.playground.presentation.components.FullScreenLoader
import com.karthik.pro.engr.github.api.playground.presentation.components.PagingList
import com.karthik.pro.engr.github.api.playground.presentation.error.PagingErrorMapper

@Composable
fun <T : Any> PagingScreenHandler(
    modifier: Modifier = Modifier,
    lazyPagingItems: LazyPagingItems<T>,
    onRetry: () -> Unit = { lazyPagingItems.retry() },
    emptyContent: @Composable () -> Unit,
    itemContent: @Composable (T) -> Unit
) {
    val refreshState = lazyPagingItems.loadState.refresh

    val isInitialLoading = refreshState is LoadState.Loading
    val isInitialError = refreshState is LoadState.Error
    val isLoaded = refreshState is LoadState.NotLoading

    val hasEmptyList = lazyPagingItems.itemSnapshotList.isEmpty()

    when {
        isInitialError && hasEmptyList -> {
            val context = LocalContext.current
            FullScreenError(
                error = PagingErrorMapper.mapPagingError(refreshState.error, context),
                onRetry = onRetry
            )
        }

        isInitialLoading && hasEmptyList -> {
            FullScreenLoader()
        }

        else -> {

            if (isLoaded && hasEmptyList) {
                emptyContent()
            } else {
                PagingList(
                    lazyPagingItems = lazyPagingItems,
                    itemContent = itemContent
                )
            }

        }
    }

}