package com.karthik.pro.engr.github.api.data.remote.dto.response

data class ReleaseDto(
    val id: Long,
    val name: String?,
    val tag_name: String,
    val body: String?,
    val published_at: String,
    val html_url: String
)