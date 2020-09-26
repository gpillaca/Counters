package com.gpillaca.counters.data.detasource

import com.gpillaca.counters.domain.Counter
import com.gpillaca.counters.ui.common.OperationResults

interface RemoteDataSource {
    suspend fun listCounters(): OperationResults<Counter>
}