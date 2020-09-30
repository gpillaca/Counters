package com.gpillaca.counters.data.datasource

import com.gpillaca.counters.domain.Counter

interface LocalDataSource {
    suspend fun saveCounters(counters: List<Counter>)
    suspend fun getAllCounters(): List<Counter>
    suspend fun isEmpty(): Boolean
    suspend fun deleteAll()
    suspend fun deleteCounter(counter: Counter)
}