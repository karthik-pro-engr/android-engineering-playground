package com.karthik.pro.engr.github.api.domain.time

import java.time.Instant

sealed class RelativeTime {
    object JustNow : RelativeTime()
    data class MinutesAgo(val value: Long) : RelativeTime()
    object Yesterday : RelativeTime()
    data class HoursAgo(val value: Long) : RelativeTime()

    data class DaysAgo(val value: Long) : RelativeTime()
    data class Date(val value: Instant) : RelativeTime()
}