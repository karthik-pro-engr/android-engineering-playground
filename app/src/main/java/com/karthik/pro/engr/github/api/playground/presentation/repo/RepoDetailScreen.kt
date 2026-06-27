package com.karthik.pro.engr.github.api.playground.presentation.repo

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.karthik.pro.engr.github.api.playground.R
import com.karthik.pro.engr.github.api.playground.presentation.components.ErrorUi
import com.karthik.pro.engr.github.api.playground.presentation.components.FullScreenLoader
import com.karthik.pro.engr.github.api.playground.presentation.components.OfflineBanner
import com.karthik.pro.engr.github.api.playground.presentation.components.SectionHeader
import com.karthik.pro.engr.github.api.playground.presentation.designsystem.Dimens
import com.karthik.pro.engr.github.api.playground.presentation.repo.components.NoData
import com.karthik.pro.engr.github.api.playground.presentation.repo.components.header.Header
import com.karthik.pro.engr.github.api.playground.presentation.repo.components.language.Language
import com.karthik.pro.engr.github.api.playground.presentation.repo.components.releases.ReleaseItem
import com.karthik.pro.engr.github.api.playground.presentation.repo.components.stats.Stats
import com.karthik.pro.engr.github.api.playground.presentation.repo.components.topics.Topics
import com.karthik.pro.engr.github.api.playground.presentation.repo.model.RepoDetailItemUi
import com.karthik.pro.engr.github.api.playground.presentation.repo.model.key

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepoDetailScreen(
    modifier: Modifier = Modifier,
    items: List<RepoDetailItemUi>,
    showStaleDataBanner: Boolean,
    repoName: String,
    onBack: () -> Unit,
    onRepoRetry: () -> Unit,
    onLanguageRetry: () -> Unit,
    onReleaseRetry: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = repoName
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack
                    ) {
                        Icon(
                            imageVector =
                                Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor =
                        MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { innerPadding ->

        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(horizontal = Dimens.large, vertical = Dimens.large)
        ) {

            if (showStaleDataBanner) {
                item {
                    OfflineBanner(resId = R.string.showing_cached_data_for_repo_detail)
                }
            }

            items(
                items = items,
                key = { item -> item.key() }
            ) { repoDetailItemUi ->


                when (repoDetailItemUi) {
                    is RepoDetailItemUi.SectionTitle -> SectionHeader(repoDetailItemUi.title)

                    is RepoDetailItemUi.HeaderError -> ErrorUi(
                        error = repoDetailItemUi.message,
                        onClick = onRepoRetry
                    )

                    RepoDetailItemUi.HeaderLoading -> FullScreenLoader()

                    is RepoDetailItemUi.HeaderSuccess -> Header(
                        headerUi = repoDetailItemUi.data
                    )

                    is RepoDetailItemUi.Stats -> Stats(statsUi = repoDetailItemUi.statsUi)

                    is RepoDetailItemUi.Topics -> {
                        if (repoDetailItemUi.topics.isEmpty()) {
                            NoData(value = R.string.no_topics)
                        } else
                            Topics(topics = repoDetailItemUi.topics)
                    }

                    is RepoDetailItemUi.LanguageError -> ErrorUi(
                        error = repoDetailItemUi.message,
                        onClick = onLanguageRetry
                    )

                    RepoDetailItemUi.LanguageLoading -> CircularProgressIndicator()

                    is RepoDetailItemUi.LanguageSuccess -> {
                        if (repoDetailItemUi.data.isEmpty()) {
                            NoData(value = R.string.no_languages)
                        } else {
                            Language(languagesList = repoDetailItemUi.data)
                        }
                    }


                    is RepoDetailItemUi.ReleaseError -> ErrorUi(
                        error = repoDetailItemUi.message,
                        onClick = onReleaseRetry
                    )

                    RepoDetailItemUi.ReleaseLoading -> CircularProgressIndicator()

                    is RepoDetailItemUi.ReleaseSuccess -> {
                        ReleaseItem(repoDetailItemUi.data) { }
                    }

                    is RepoDetailItemUi.NoReleases -> {
                        NoData(value = R.string.no_releases)
                    }

                }
            }

        }

    }
}


