package com.karthik.pro.engr.github.api.playground.presentation.error

import android.content.Context
import androidx.annotation.StringRes
import com.karthik.pro.engr.github.api.data.remote.error.ApiException
import com.karthik.pro.engr.github.api.playground.R
import okio.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException


object PagingErrorMapper {

    fun mapPagingError(
        error: Throwable,
        context: Context
    ): String {
        return when (error) {
            is ApiException -> {
                error.message ?: context.getString(R.string.error_generic)
            }

            is UnknownHostException ->
                context.getString(R.string.error_no_internet)

            is SocketTimeoutException ->
                context.getString(R.string.error_timeout)

            is IOException -> {
                context.getString(R.string.error_connection)
            }

            else -> context.getString(R.string.error_generic)
        }
    }
}