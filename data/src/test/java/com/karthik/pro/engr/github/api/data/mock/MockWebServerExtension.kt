package com.karthik.pro.engr.github.api.data.mock

import mockwebserver3.MockResponse
import mockwebserver3.MockWebServer
import okhttp3.Headers
import okhttp3.Headers.Companion.headersOf

fun Map<String, String>.toHeaders(): Headers = if (isEmpty()) headersOf() else
    headersOf(*flatMap { listOf(it.key, it.value) }.toTypedArray())

fun MockWebServer.enqueueResponse(
    code: Int,
    body: String? = null,
    headers: Map<String, String> = emptyMap(),
    configurable: MockResponse.Builder.() -> MockResponse.Builder = { this }
) {

    val response = MockResponse(
        code = code,
        headers = headers.toHeaders(),
        body = body.orEmpty()
    ).newBuilder()
        .configurable()
        .build()

    enqueue(response)

}