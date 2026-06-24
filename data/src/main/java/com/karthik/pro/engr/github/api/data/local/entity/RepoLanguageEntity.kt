package com.karthik.pro.engr.github.api.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    primaryKeys = [
        "repoId",
        "language"
    ],
    foreignKeys = [
        ForeignKey(
            entity = RepoEntity::class,
            parentColumns = ["id"],
            childColumns = ["repoId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("repoId")
    ]
)
data class RepoLanguageEntity(
    val repoId: Long,
    val language: String,
    val bytes: Long
)