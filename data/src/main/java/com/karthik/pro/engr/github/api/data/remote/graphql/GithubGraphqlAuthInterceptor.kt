package com.karthik.pro.engr.github.api.data.remote.graphql

import com.karthik.pro.engr.github.api.data.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class GithubGraphqlAuthInterceptor(
    private val token: String
) : Interceptor {

    init {
        check(
            BuildConfig.GRAPHQL_TOKEN.isNotBlank()
        ) {
            "GRAPHQL_TOKEN is missing"
        }
    }

    override fun intercept(
        chain: Interceptor.Chain
    ): Response {

        val request = chain.request()
            .newBuilder()
            .addHeader(
                "Authorization",
                "Bearer $token"
            )
            .build()

        return chain.proceed(request)
    }
}