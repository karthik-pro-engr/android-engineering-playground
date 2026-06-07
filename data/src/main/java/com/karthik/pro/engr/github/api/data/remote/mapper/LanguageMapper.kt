package com.karthik.pro.engr.github.api.data.remote.mapper

import com.karthik.pro.engr.github.api.domain.calculator.PercentageCalculator
import com.karthik.pro.engr.github.api.domain.model.Language
import kotlin.collections.component1
import kotlin.collections.component2

fun Map<String, Long>.toLanguageList(): List<Language> {
    val total = values.sum()
    return map { (name, bytes) ->
        Language(
            name = name,
            percentage = PercentageCalculator.calculate(bytes, total)
        )
    }.sortedByDescending { it.percentage }
}