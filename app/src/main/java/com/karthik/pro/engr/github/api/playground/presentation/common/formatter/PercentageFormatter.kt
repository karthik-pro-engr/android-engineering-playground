package com.karthik.pro.engr.github.api.playground.presentation.common.formatter

object PercentageFormatter {

    fun format(value: Float): String {
        return "${value.toInt()}%"
    }
}