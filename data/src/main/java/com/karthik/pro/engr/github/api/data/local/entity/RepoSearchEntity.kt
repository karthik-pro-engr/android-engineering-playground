package com.karthik.pro.engr.github.api.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RepoSearchEntity(
    @PrimaryKey
    val username: String,

    val ownerId: Long,

    val avatarUrl: String?,

    val profileUrl: String?,

    val lastSync: Long,

    val lastAccessed: Long
)