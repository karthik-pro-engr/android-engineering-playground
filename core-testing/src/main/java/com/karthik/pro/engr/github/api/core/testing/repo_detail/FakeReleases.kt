package com.karthik.pro.engr.github.api.core.testing.repo_detail

import com.karthik.pro.engr.github.api.domain.model.Asset
import com.karthik.pro.engr.github.api.domain.model.Release

object FakeReleases {
    fun releases() = listOf(
        Release(
            id = 294359169,
            title = "v1.0.0-TEST-12345",
            version = "v1.0.0-TEST-12345",
            description = "test release for upload",
            date = "2026-03-08T07:01:28Z"
        ),
        Release(
            id = 294354682,
            title = "v1.0.0-TEST-1234",
            version = "v1.0.0-TEST-1234",
            description = "test release for upload",
            date = "2026-03-08T06:10:55Z"
        ),
        Release(
            id = 294166063,
            title = "v1.0.0-TEST-123",
            version = "v1.0.0-TEST-123",
            description = "test release for upload",
            date = "2026-03-07T02:15:07Z"
        )
    )

    fun releaseItem() = Release(
        id = 294166063,
        title = "v1.0.0-TEST-123",
        version = "v1.0.0-TEST-123",
        description = "test release for upload",
        date = "2026-03-07T02:15:07Z"
    )
}