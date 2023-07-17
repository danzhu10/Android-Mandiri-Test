package com.mandiri.test.remote.interceptor

import com.mandiri.test.BuildConfig
import com.mandiri.test.common.Constants.QUERY_PARAM_API_KEY
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class ApiKeyInterceptor @Inject constructor() :
    Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val url = chain.request().url.newBuilder()
            .addQueryParameter(QUERY_PARAM_API_KEY, BuildConfig.MOVIE_API_KEY)
            .build()

        val request = chain.request().newBuilder()
            .url(url)
            .build()

        return chain.proceed(request)
    }
}