package com.karthik.pro.engr.github.api.data.remote.mapper

import com.karthik.pro.engr.github.api.data.local.entity.RepoLanguageEntity
import com.karthik.pro.engr.github.api.domain.calculator.PercentageCalculator
import com.karthik.pro.engr.github.api.domain.model.Language


object RepoLanguageMapper {

    fun fromApi(
        repoId: Long,
        languages: Map<String, Long>
    ): List<RepoLanguageEntity> {

        return languages.map { (name, bytes) ->

            RepoLanguageEntity(
                repoId = repoId,
                language = name,
                bytes = bytes
            )
        }
    }

    fun toDomain(
        entities: List<RepoLanguageEntity>
    ): List<Language> {

        val totalBytes = entities.sumOf { it.bytes }

        return entities.map {

            Language(
                name = it.language,
                percentage = PercentageCalculator.calculate(
                     it.bytes,
                    totalBytes
                )
            )
        }.sortedByDescending {
            it.percentage
        }
    }
}