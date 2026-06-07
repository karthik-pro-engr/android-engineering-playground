package com.karthik.pro.engr.github.api.domain.time

interface DateFormatter {

    fun format(isoDate: String): RelativeTime
}