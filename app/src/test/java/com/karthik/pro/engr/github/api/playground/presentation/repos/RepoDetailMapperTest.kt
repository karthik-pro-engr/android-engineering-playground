package com.karthik.pro.engr.github.api.playground.presentation.repos

import com.google.common.truth.Truth.assertThat
import com.karthik.pro.engr.github.api.playground.presentation.common.formatter.NumberFormatter
import org.junit.Test

class RepoDetailMapperTest {
    @Test
    fun `check stars to readable`() {

        assertThat("2.6k").isEqualTo(NumberFormatter.readableCount(2639))
        assertThat("1.1k").isEqualTo(NumberFormatter.readableCount(1123))
        assertThat("1k").isEqualTo(NumberFormatter.readableCount(1001))
        assertThat("1k").isEqualTo(NumberFormatter.readableCount(1000))
        assertThat("999").isEqualTo(NumberFormatter.readableCount(999))


    }
}