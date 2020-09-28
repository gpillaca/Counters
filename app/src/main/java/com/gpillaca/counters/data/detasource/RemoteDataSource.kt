package com.gpillaca.counters.data.detasource

import com.gpillaca.counters.domain.Counter
import com.gpillaca.counters.ui.common.OperationResults

interface RemoteDataSource {
    suspend fun listCounters(): OperationResults<Counter>
    suspend fun addCounter(title: String): OperationResults<Counter>
    suspend fun incrementCounter(id: String): OperationResults<Counter>
    suspend fun decrementCounter(id: String): OperationResults<Counter>
}