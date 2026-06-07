package com.karthik.pro.engr.github.api.playground.presentation.common.formatter

import kotlin.math.floor

object NumberFormatter {

    fun readableCount(value: Int): String {
        if (value < 1000) return value.toString()

        val result = floor(value / 100.0) / 10

        return if (result % 1.0 == 0.0) {
            "${result.toInt()}k"
        } else {
            "${result}k"
        }
    }
}