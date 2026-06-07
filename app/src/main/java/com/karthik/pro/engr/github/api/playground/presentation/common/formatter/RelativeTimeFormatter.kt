package com.karthik.pro.engr.github.api.playground.presentation.common.formatter

import com.karthik.pro.engr.github.api.domain.time.DateFormatter
import com.karthik.pro.engr.github.api.domain.time.RelativeTime
import java.time.Clock
import java.time.Duration
import java.time.Instant
import java.time.format.DateTimeFormatter

class RelativeTimeFormatter(private val clock: Clock = Clock.systemDefaultZone()) : DateFormatter {
    override fun format(isoDate: String): RelativeTime {
        val instant = Instant.parse(isoDate)
        val now = Instant.now(clock)

        val duration = Duration.between(instant, now)

        val seconds = duration.seconds
        val minutes = duration.toMinutes()
        val hours = duration.toHours()
        val days = duration.toDays()

        return when {
            seconds < 60 -> RelativeTime.JustNow

            minutes < 60 ->
                RelativeTime.MinutesAgo(minutes)

            hours < 24 -> RelativeTime.HoursAgo(hours)

            days == 1L -> RelativeTime.Yesterday

            days < 7 -> RelativeTime.DaysAgo(days)

            else -> {
                RelativeTime.Date(instant)
            }
        }
    }
}