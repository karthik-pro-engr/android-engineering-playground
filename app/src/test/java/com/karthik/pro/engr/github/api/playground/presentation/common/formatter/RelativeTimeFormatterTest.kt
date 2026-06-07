package com.karthik.pro.engr.github.api.playground.presentation.common.formatter

import com.google.common.truth.Truth.assertThat
import com.karthik.pro.engr.github.api.domain.time.RelativeTime
import org.junit.Test
import java.time.Clock
import java.time.Duration

import java.time.Instant
import java.time.ZoneId


class RelativeTimeFormatterTest {
    private val fixedNow = Instant.parse("2024-01-01T00:00:00Z")
    private val clock = Clock.fixed(fixedNow, ZoneId.of("UTC"))
    private val formatter = RelativeTimeFormatter(clock)

    @Test
    fun `when duration is belo 60 seconds should return Just Now`() {

        val transformed =
            formatter.format(secondsAgo(10).toString())

        assertThat(transformed).isEqualTo(RelativeTime.JustNow)
    }

    @Test
    fun `when duration is below 60 minutes should return how many minutes`() {

        val twoMinutesBefore = formatter.format(secondsAgo(70).toString())
        assertThat(twoMinutesBefore).isEqualTo(RelativeTime.MinutesAgo(1))


        val fiftyNineMinutes = formatter.format(minutesAgo(59).toString())
        assertThat(fiftyNineMinutes).isEqualTo(RelativeTime.MinutesAgo(59))

    }

    @Test
    fun `when duration is below 24 hours should return hours`() {
        val result = formatter.format(hoursAgo(23).toString())

        assertThat(result).isEqualTo(RelativeTime.HoursAgo(23))
    }

    @Test
    fun `when duration is between 24 and 48 hours should return Yesterday`() {
        val result = formatter.format(daysAgo(1).toString())

        assertThat(result).isEqualTo(RelativeTime.Yesterday)
    }

    @Test
    fun `when duration is below 7 days should return in days`() {
        val result = formatter.format(daysAgo(6).toString())

        assertThat(result).isEqualTo(RelativeTime.DaysAgo(6))
    }

    private fun secondsAgo(seconds: Long) = fixedNow.minus(Duration.ofSeconds(seconds))
    private fun minutesAgo(minutes: Long) = fixedNow.minus(Duration.ofMinutes(minutes))
    private fun hoursAgo(hours: Long) = fixedNow.minus(Duration.ofHours(hours))
    private fun daysAgo(days: Long) = fixedNow.minus(Duration.ofDays(days))


}
