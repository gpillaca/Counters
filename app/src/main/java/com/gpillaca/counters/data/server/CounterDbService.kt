package com.gpillaca.counters.data.server

import retrofit2.http.*

interface CounterDbService {
    @GET("api/v1/counters")
    suspend fun listCounters(): List<Counter>

    @POST("api/v1/counter")
    suspend fun addCounter(@Body data: Map<String, String>): List<Counter>

    @POST("api/v1/counter/inc")
    suspend fun incrementCounter(@Body data: Map<String, String>): List<Counter>

    @POST("api/v1/counter/dec")
    suspend fun decrementCounter(@Body data: Map<String, String>): List<Counter>
}