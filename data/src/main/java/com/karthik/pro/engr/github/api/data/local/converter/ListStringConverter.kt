package com.karthik.pro.engr.github.api.data.local.converter

import androidx.room.TypeConverter
import kotlinx.serialization.json.Json

class ListStringConverter {

    @TypeConverter
    fun fromTopics(topics: List<String>): String =
        Json.encodeToString(topics)

    @TypeConverter
    fun toTopics(value: String): List<String> = Json.decodeFromString(value)
}
