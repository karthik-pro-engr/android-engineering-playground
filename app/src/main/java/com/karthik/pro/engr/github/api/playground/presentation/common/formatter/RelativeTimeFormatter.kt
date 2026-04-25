package com.karthik.pro.engr.github.api.playground.presentation.common.formatter

import java.time.Duration
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class RelativeTimeFormatter : DateFormatter {
    override fun format(isoDate: String): String {
        val instant = Instant.parse(isoDate)
        val now = Instant.now()

        val duration = Duration.between(instant, now)

        val seconds = duration.seconds
        val minutes = duration.toMinutes()
        val hours = duration.toHours()
        val days = duration.toDays()

        return when {
            seconds < 60 -> "Just now"

            minutes < 60 -> "$minutes minutes ago"

            hours < 24 -> "$hours hours ago"

            days == 1L -> "Yesterday"

            days < 7 -> "$days days ago"

            else -> {
                val formatter = DateTimeFormatter.ofPattern("MMM d, yyyy")
                    .withZone(ZoneId.systemDefault())

                formatter.format(instant)
            }
        }
    }
}