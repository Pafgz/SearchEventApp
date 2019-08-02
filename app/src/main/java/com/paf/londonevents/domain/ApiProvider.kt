package com.paf.londonevents.domain

import com.paf.londonevents.Environment
import com.paf.londonevents.data.common.ConverterFactory
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

object ApiProvider {

    fun getDiscoveryApi(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Environment.DISCOVERY_API_BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(ConverterFactory())
            .client(HttpClient.discoveryClient)
            .build()
    }
}