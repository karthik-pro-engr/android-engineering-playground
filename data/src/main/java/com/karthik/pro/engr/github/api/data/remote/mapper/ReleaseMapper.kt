package com.karthik.pro.engr.github.api.data.remote.mapper

import com.karthik.pro.engr.github.api.data.remote.dto.response.ReleaseDto
import com.karthik.pro.engr.github.api.domain.model.Release

object ReleaseMapper {
    fun fromRelease(dto: ReleaseDto): Release {
        return with(dto) {
            Release(
                id = id,
                title = name.orEmpty(),
                version = tag_name,
                description = body.orEmpty(),
                date = published_at
            )
        }
    }

    fun fromReleaseDtoList(dtos: List<ReleaseDto>): List<Release> = dtos.map { fromRelease(it) }
}
