package com.karthik.pro.engr.github.api.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RepoSearchEntity(
    @PrimaryKey
    val username: String,
    val lastSync: Long,
    val lastAccessed: Long
)