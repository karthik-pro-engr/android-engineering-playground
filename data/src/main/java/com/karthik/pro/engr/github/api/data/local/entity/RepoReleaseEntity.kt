package com.karthik.pro.engr.github.api.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
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
data class RepoReleaseEntity(

    @PrimaryKey
    val id: Long,

    val repoId: Long,

    val title: String,

    val version: String,

    val description: String,

    val date: String
)