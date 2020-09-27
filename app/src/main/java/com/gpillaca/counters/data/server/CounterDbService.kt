package com.gpillaca.counters.data.server

import retrofit2.http.*

interface CounterDbService {
    @GET("api/v1/counters")
    suspend fun listCounters(): List<Counter>

    @Headers("Content-Type: application/json")
    @POST("api/v1/counter")
    suspend fun addCounter(@Body data: Map<String, String>): List<Counter>
}