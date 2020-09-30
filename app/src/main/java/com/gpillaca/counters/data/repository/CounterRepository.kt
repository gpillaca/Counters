package com.gpillaca.counters.data.repository

import com.gpillaca.counters.domain.Counter
import com.gpillaca.counters.ui.common.OperationResults

interface CounterRepository {
    suspend fun listCounters(forceUpdate: Boolean): OperationResults<Counter>
    suspend fun addCounter(title: String): OperationResults<Counter>
    suspend fun increment(id: String): OperationResults<Counter>
    suspend fun decrement(id: String): OperationResults<Counter>
    suspend fun deleteCounter(counter: Counter): OperationResults<Counter>
}