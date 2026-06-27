package com.karthik.pro.engr.github.api.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [ForeignKey(
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
data class RepoEntity(
    @PrimaryKey
    val id: Long,
    val name: String,
    val description: String?,
    val language:String?,
    val stars: Int,
    val forks: Int,
    val topics: List<String>,
    val username: String,
)