package com.paf.londonevents.domain

import com.paf.londonevents.Environment
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response

object HttpClient {

    val discoveryClient: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(ApiKeyInterceptor())
        .build()

}

class ApiKeyInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val url = chain.request().url().newBuilder()
            .addQueryParameter("apikey", Environment.API_KEY)
            .build()

        val request = chain.request().newBuilder().url(url).build()
        return chain.proceed(request)
    }

}