package com.karthik.pro.engr.github.api.playground.presentation.error

import com.karthik.pro.engr.github.api.domain.error.DomainError

object ApiErrorMapper {
    fun parseError(error: DomainError): String {
        return when (error) {
            DomainError.Network -> "Network Error"
            DomainError.NotFound -> "Not Found"
            DomainError.RateLimited -> "Rate Limited"
            DomainError.Unauthorized -> "UnAuthorized"
            DomainError.Unknown -> "Unknown"
        }
    }
}