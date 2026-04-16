package com.karthik.pro.engr.github.api.playground.presentation.repo.components.language

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.karthik.pro.engr.devtools.AllVariantsPreview
import com.karthik.pro.engr.github.api.playground.presentation.components.ErrorUi
import com.karthik.pro.engr.github.api.playground.presentation.error.ApiErrorMapper
import com.karthik.pro.engr.github.api.playground.presentation.repo.RepoDetailTestTags
import com.karthik.pro.engr.github.api.playground.presentation.repo.RepoDetailTestTags.PROGRESS_INDICATOR
import com.karthik.pro.engr.github.api.playground.presentation.repo.components.language.model.LanguageUi
import com.karthik.pro.engr.github.api.playground.presentation.uistate.ListUiState


@Composable
fun Language(
    modifier: Modifier = Modifier,
    uiState: ListUiState<LanguageUi>
) {
    when (uiState) {
        is ListUiState.Error -> ErrorUi(error = ApiErrorMapper.parseError(uiState.error)) { }
        is ListUiState.Loading -> CircularProgressIndicator(
            modifier = Modifier.testTag(
                PROGRESS_INDICATOR
            )
        )

        is ListUiState.Success -> {
            FlowRow(
                modifier = Modifier
                    .testTag(RepoDetailTestTags.LANGUAGE_LIST)
                    .fillMaxWidth()
            ) {
                uiState.data.forEach { languageUi ->
                    Card(
                        modifier = modifier
                            .testTag(RepoDetailTestTags.LANGUAGE)
                            .padding(5.dp)
                    ) {
                        Column(Modifier.padding(12.dp)) {
                            with(languageUi) {
                                Text(language, style = MaterialTheme.typography.bodyMedium)
                                Text(
                                    "$percentage%",
                                    style = MaterialTheme.typography.headlineSmall
                                )
                            }
                        }

                    }
                }
            }
        }
    }


}

@AllVariantsPreview
@Composable
private fun LanguagePreview() {
    Language(
        uiState = ListUiState.Success(
            listOf(
                LanguageUi("Kotlin", "99.4"),
                LanguageUi("Java", "0.4"), LanguageUi("Kotlin", "99.4"),
                LanguageUi("Java", "0.4"), LanguageUi("Kotlin", "99.4"),
                LanguageUi("Java", "0.4"), LanguageUi("Kotlin", "99.4"),
                LanguageUi("Java", "0.4")
            )
        )
    )
}