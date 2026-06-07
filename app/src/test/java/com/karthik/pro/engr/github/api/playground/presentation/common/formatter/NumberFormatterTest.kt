package com.karthik.pro.engr.github.api.playground.presentation.common.formatter

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class NumberFormatterTest {

    @Test
    fun `values below 1000 should return exact number`() {
        assertThat(NumberFormatter.readableCount(0)).isEqualTo("0")
        assertThat(NumberFormatter.readableCount(999)).isEqualTo("999")
    }

    @Test
    fun `1000 should return 1k`() {
        assertThat(NumberFormatter.readableCount(1099)).isEqualTo("1k")
    }

    @Test
    fun `values should be truncated not rounded`() {
        assertThat(NumberFormatter.readableCount(1499)).isEqualTo("1.4k")
        assertThat(NumberFormatter.readableCount(1599)).isEqualTo("1.5k")
        assertThat(NumberFormatter.readableCount(1999)).isEqualTo("1.9k")
    }

    @Test
    fun `values should return rounded number with k format`() {
        assertThat(NumberFormatter.readableCount(1100)).isEqualTo("1.1k")
        assertThat(NumberFormatter.readableCount(1500)).isEqualTo("1.5k")
    }

    @Test
    fun `large values should still use k format`() {
        assertThat(NumberFormatter.readableCount(10000)).isEqualTo("10k")
        assertThat(NumberFormatter.readableCount(1000000)).isEqualTo("1000k")
    }

}