package com.karthik.pro.engr.github.api.playground.presentation.repo.components.language

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.karthik.pro.engr.devtools.AllVariantsPreview
import com.karthik.pro.engr.github.api.core.testing.RepoFactory
import com.karthik.pro.engr.github.api.data.remote.mapper.toLanguageList
import com.karthik.pro.engr.github.api.domain.model.Language
import com.karthik.pro.engr.github.api.playground.presentation.common.formatter.PercentageFormatter
import com.karthik.pro.engr.github.api.playground.presentation.designsystem.Dimens


@Composable
fun Language(languagesList: List<Language>) {

    val max = languagesList.maxOfOrNull { it.percentage } ?: 1f

    Column(modifier = Modifier.fillMaxWidth(),verticalArrangement = Arrangement.spacedBy(Dimens.medium)) {

        languagesList.forEach { language ->

            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(language.name)
                    Text(PercentageFormatter.format(language.percentage))
                }

                Spacer(modifier = Modifier.height(4.dp))

                LinearProgressIndicator(
                    progress = { language.percentage / max },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                )
            }
        }
    }
}


@AllVariantsPreview
@Composable
private fun LanguagePreview() {
    Language(
        languagesList = RepoFactory.defaultLanguages().toLanguageList()
    )
}