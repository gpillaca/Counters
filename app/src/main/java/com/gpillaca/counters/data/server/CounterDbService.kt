package com.gpillaca.counters.data.server

import retrofit2.http.GET

interface CounterDbService {
    @GET("api/v1/counters")
    suspend fun listCounters(): List<Counter>
}