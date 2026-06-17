package com.karthik.pro.engr.github.api.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = RepoSearchEntity::class,
            parentColumns = ["username"],
            childColumns = ["username"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("username")
    ]
)
data class RemoteKeysEntity(
    @PrimaryKey
    val username: String,
    val nextPage: Int?,
    val lastUpdated: Long
)