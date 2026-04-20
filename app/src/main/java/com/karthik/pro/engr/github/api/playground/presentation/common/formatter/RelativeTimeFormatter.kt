package com.karthik.pro.engr.github.api.playground.presentation.common.formatter

class RelativeTimeFormatter : DateFormatter {
    override fun format(isoDate: String): String {
        return formatRelativeTime(isoDate)
    }
}