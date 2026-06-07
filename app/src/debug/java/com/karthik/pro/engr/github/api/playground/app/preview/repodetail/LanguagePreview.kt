package com.karthik.pro.engr.github.api.playground.app.preview.repodetail

import androidx.compose.runtime.Composable
import com.karthik.pro.engr.devtools.AllVariantsPreview
import com.karthik.pro.engr.github.api.core.testing.RepoFactory
import com.karthik.pro.engr.github.api.data.remote.mapper.toLanguageList
import com.karthik.pro.engr.github.api.playground.presentation.repo.components.language.Language

@AllVariantsPreview
@Composable
private fun LanguagePreview() {
    Language(
        languagesList = RepoFactory.defaultLanguages().toLanguageList()
    )
}