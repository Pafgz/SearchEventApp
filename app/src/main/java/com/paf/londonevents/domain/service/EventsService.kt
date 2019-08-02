package com.paf.londonevents.domain.service

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface EventsService {

    @GET("events")
    fun fetchEvents(@QueryMap params: Map<String, String>): Call<String>
}