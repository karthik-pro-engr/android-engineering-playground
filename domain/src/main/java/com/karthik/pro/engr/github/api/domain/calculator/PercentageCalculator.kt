package com.karthik.pro.engr.github.api.domain.calculator

object PercentageCalculator {

    fun calculate(bytes: Long, total: Long): Float {
        if (total == 0L) return 0f
        return (bytes.toFloat() / total) * 100f
    }
}